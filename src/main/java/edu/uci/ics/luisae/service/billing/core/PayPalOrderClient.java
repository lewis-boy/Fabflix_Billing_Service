package edu.uci.ics.luisae.service.billing.core;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.sdk.v1.payments.Payment;

public class PayPalOrderClient {
    private static final String clientId = "AX7AbJts4JhyEm7UaJzAXlVZ12yOUp-zrgdTTvj9D9RJw5siceOAs8oExECBH0UlXJT84j6Mav58Vuum";
    private static final String clientSecret = "EHW-5Yebfln8aKVcgz6WgMTCQO1spKeGmeMDTiyCJMIov-Ni8sAPKZPbTekeg5FEb52Al7z-hHp41Obb";

    public PayPalEnvironment environment =
            new PayPalEnvironment.Sandbox(clientId,clientSecret);
    public PayPalHttpClient client = new PayPalHttpClient(environment);
}
