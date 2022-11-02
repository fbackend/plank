package com.mistra.plank.handler;

import com.mistra.plank.api.TradeResultVo;
import com.mistra.plank.api.request.*;
import com.mistra.plank.api.response.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("crGridStrategyHandler")
public class CrGridStrategyHandler extends GridStrategyHandler {

    @Override
    public TradeResultVo<GetOrdersDataResponse> getOrderData(int userId) {
         TradeResultVo<CrGetOrdersDataResponse> tradeResultVo = tradeApiService.crGetOrdersData(new CrGetOrdersDataRequest(userId));
         return buildResult(tradeResultVo);
    }

    @Override
    public TradeResultVo<GetDealDataResponse> getDealData(int userId) {
        TradeResultVo<CrGetDealDataResponse> tradeResultVo =  tradeApiService.crGetDealData(new CrGetDealDataRequest(userId));
         return buildResult(tradeResultVo);
    }

    @Override
    protected TradeResultVo<SubmitResponse> submit(SubmitRequest request) {
        CrSubmitRequest crRequest = new CrSubmitRequest(request.getUserId());
        BeanUtils.copyProperties(request, crRequest);

        if (SubmitRequest.B.equals(request.getTradeType())) {
            crRequest.setTradeInfo(CrSubmitRequest.xyjylx_rz_b);
        } else {
            crRequest.setTradeInfo(CrSubmitRequest.xyjylx_hk_s);
        }

        TradeResultVo<CrSubmitResponse> tradeResultVo = tradeApiService.crSubmit(crRequest);
        return buildResult(tradeResultVo);
    }

    @Override
    protected TradeResultVo<RevokeResponse> revoke(int userId, String revokes) {
        CrRevokeRequest request = new CrRevokeRequest(userId);
        request.setRevokes(revokes);

        TradeResultVo<CrRevokeResponse> tradeResultVo = tradeApiService.crRevoke(request);
        return buildResult(tradeResultVo);
    }

    @Override
    protected String getFlag() {
        return "cr";
    }

    private <T> TradeResultVo<T> buildResult(TradeResultVo<? extends T> tradeResultVo) {
        TradeResultVo<T> resultVo =  new TradeResultVo<>();
         resultVo.setStatus(tradeResultVo.getStatus());
         resultVo.setMessage(tradeResultVo.getMessage());
         if (tradeResultVo.success()) {
             resultVo.setData(tradeResultVo.getData().stream().map(v -> v).collect(Collectors.toList()));
         }
         return resultVo;
    }

}
