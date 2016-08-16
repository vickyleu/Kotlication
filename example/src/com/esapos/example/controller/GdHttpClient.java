package com.esapos.example.controller;

import android.util.Log;

import com.esapos.lib.model.Component.Http.HttpModule;
import com.esapos.lib.model.Component.Http.HttpResponseFactory;
import com.esapos.lib.model.Component.HttpLibrary.HttpResponseModel;
import com.esapos.example.R;
import com.esapos.example.controller.Component.HttpClient;
import com.esapos.example.model.Http.Req.CheckVersionReq;
import com.esapos.example.model.Http.Req.QueryTransReq;
import com.esapos.example.model.Http.Req.SignReq;
import com.esapos.example.model.Http.Resp.BalanceQueryResp;
import com.esapos.example.model.Http.Resp.CheckVersionResp;
import com.esapos.example.model.Http.Resp.QueryTransResp;
import com.esapos.example.model.Http.Resp.SaleResp;
import com.esapos.example.model.Http.Resp.SignResp;
import com.esapos.example.model.Http.Resp.UnSaleResp;
import com.esapos.example.model.Http.Resp.UpdateTermkeyResp;
import com.esapos.example.model.Http.Resp.UpdateWorkKeyResp;
import com.esapos.example.model.IConstant;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/23.
 */
public class GdHttpClient extends HttpClient {
    private static final String TAG = GdHttpClient.class.getSimpleName();
    static GdHttpClient client;

    public static synchronized GdHttpClient getInstance() {
        if (client == null) client = new GdHttpClient();
        return client;
    }

    @Override
    protected String BaseUrl() {
        return IConstant.BASE_URL;
    }

    @Override
    protected JSONObject SetDataByYourSelf(int option, JSONObject params, Object[] object) throws Exception {
        putIntToJson(params, getString(R.string.commandId), option);
        JSONObject body = new JSONObject();
        switch (option) {
            case IConstant.commandId.UpdateWorkKeyReq:
                body.put(getString(R.string.voucherNo), new Date().getTime());
                putStringToJson(body, getString(R.string.sn), AppCenter.getInstance().getSn());
                break;

            case IConstant.commandId.UpdateTermkeyReq:
                putStringToJson(body, getString(R.string.sn), AppCenter.getInstance().getSn());
                break;
            case IConstant.commandId.CheckVersionReq:
                CheckVersionReq req = (CheckVersionReq) object[0];
                putStringToJson(params, getString(R.string.reqNode), req.getReqNode());
                putStringToJson(body, getString(R.string.pkgName), req.getPkgName());
                putStringToJson(body, getString(R.string.verName), req.getVerName());
                putIntToJson(body, getString(R.string.verCode), req.getVerCode());
//                putIntToJson(body, getString(R.string.merId), req.getMerId());
                break;
            case IConstant.commandId.BalanceQueryReq:
                putObjToJson(body, object[0]);
                break;
            case IConstant.commandId.SaleReq:
                putObjToJson(body, object[0]);
                putStringToJson(body, getString(R.string.amount), (String) object[1]);
                break;
            case IConstant.commandId.UnSaleReq:
                putObjToJson(body, object[0]);
                putStringToJson(body, getString(R.string.referenceNo), (String) object[1]);
                break;
            case IConstant.commandId.QueryTransReq:
                QueryTransReq transReq = (QueryTransReq) object[0];
                putStringToJson(body, getString(R.string.sn), AppCenter.getInstance().getSn());
                putStringToJson(body, getString(R.string.startTime), transReq.getStartTime());
                putStringToJson(body, getString(R.string.endTime), transReq.getEndTime());
                putIntToJson(body, getString(R.string.pageSize), transReq.getPageSize());
                putIntToJson(body, getString(R.string.pageIndex), transReq.getPageIndex());
                putStringToJson(body, getString(R.string.transType), transReq.getTransType());
                break;
            case IConstant.commandId.SignReq:
                SignReq signReq = (SignReq) object[0];
                putStringToJson(body, getString(R.string.tradeDate), signReq.getTradeDate());
                putStringToJson(body, getString(R.string.tradeTime), signReq.getTradeTime());
                putStringToJson(body, getString(R.string.referenceNo), signReq.getReferenceNo());
                putStringToJson(body, getString(R.string.signImage), signReq.getSignImage());
                putStringToJson(body, getString(R.string.sn), AppCenter.getInstance().getSn());
                break;
        }

        params.put(getString(R.string.body), body);

        return params;
    }

    @Override
    public HttpModule ResponseDataPick(HttpResponseModel var1, JSONObject json) {
        super.ResponseDataPick(var1, json);
        HttpModule module = null;
        int optionId = json != null ? json.optInt("commandId") : -1;
        switch (optionId) {
            case IConstant.commandId.UpdateWorkKeyResp:
                module = HttpResponseFactory.obj().
                        buildModule(new UpdateWorkKeyResp(), var1);
                break;
            case IConstant.commandId.SignResp:
                module = HttpResponseFactory.obj().
                        buildModule(new SignResp(), var1);
                break;
            case IConstant.commandId.QueryTransResp:
                module = HttpResponseFactory.obj().
                        buildModule(new QueryTransResp(), var1);
                break;
            case IConstant.commandId.CheckVersionResp:
                module = HttpResponseFactory.obj().
                        buildModule(new CheckVersionResp(), var1);
                break;
            case IConstant.commandId.BalanceQueryResp:
                module = HttpResponseFactory.obj().
                        buildModule(new BalanceQueryResp(), var1);
                break;
            case IConstant.commandId.UnSaleResp:
                module = HttpResponseFactory.obj().
                        buildModule(new UnSaleResp(), var1);
                break;
            case IConstant.commandId.SaleResp:
                module = HttpResponseFactory.obj().
                        buildModule(new SaleResp(), var1);
                break;
            case IConstant.commandId.UpdateTermkeyResp:
                module = HttpResponseFactory.obj().
                        buildModule(new UpdateTermkeyResp(), var1);
            default:
                Log.e(TAG, "httpSuccess: " + new String(var1.getResponse()));
                break;
        }
        return module;
    }
}
