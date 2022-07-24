/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.json;

import org.apache.http.client.methods.HttpPost;

/**
 *
 * @author Moubarak
 */
public class HttpDeletev2 extends HttpPost {

    public HttpDeletev2(String uri) {
        super(uri);
    }

    /**
     *
     * @return
     */
    @Override
    public String getMethod() {
        return "DELETE";

    }

}
