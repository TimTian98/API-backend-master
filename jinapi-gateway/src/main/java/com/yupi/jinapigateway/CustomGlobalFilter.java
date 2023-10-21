package com.yupi.jinapigateway;

import com.yupi.jinapiclientsdk.utils.SignUtils;
import com.yupi.jinapicommon.model.entity.InterfaceInfo;
import com.yupi.jinapicommon.model.entity.User;
import com.yupi.jinapicommon.service.InnerInterfaceInfoService;
import com.yupi.jinapicommon.service.InnerUserInterfaceInfoService;
import com.yupi.jinapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.请求日志输出
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST+request.getPath().value();
        String method = request.getMethod().toString();

        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        ServerHttpResponse response = exchange.getResponse();
        // 2. 访问控制 - 黑白名单
        if (!IP_WHITE_LIST.contains("127.0.0.1")) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // 3. 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        //根据数据库查看是否ak是否分配给用户
        User invokeUser = null;

        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }


        //  校验随机数，后端存储可以用hashmap或redis
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        //  校验时间
        Long currentTime = System.currentTimeMillis() / 1000;
        Long FIVE_MIN = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) > FIVE_MIN) {
            return handleNoAuth(response);
        }

        // 实际从数据库中拿sk
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }

        // 4. 判断接口是否存在
        //  从数据库中查询接口，以及请求方法是否匹配
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //todo 是否还有调用次数


        // 5. 请求转发，模拟调用接口
        //Mono<Void> filter = chain.filter(exchange);
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());


//        return filter;
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            //获取原始响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            //获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            //判断状态码是否为200 ok
            if (statusCode == HttpStatus.OK) {
                //创建一个装饰后的响应对象
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    //重写writeWith方法，用于处理响应体的数据
                    //这段方法就是只要当我们的迷你接口调用完成，等它返回结果
                    //就会调用writeWith方法，我i们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        //判断响应体是否为Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 7. todo 调用成功，接口调用次数+1
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                //读取响应体的内容并转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info("响应结果：" + data);
                                //将处理好的内容重新包装成DataBuffer并返回
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8.调用失败返回规范错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                //对于200 ok的请求，将装饰后的响应对象传递给下一个过滤器链，并继续处理（Response对象为装饰过的）
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            //对于非200 ok 的请求直接返回，进行降级处理
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}

