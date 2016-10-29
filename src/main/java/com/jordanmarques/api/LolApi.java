package com.jordanmarques.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordanmarques.api.model.Queue;
import com.jordanmarques.api.model.Summoner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

public class LolApi {

    private final String LOL_API_KEY = "RGAPI-7a7dc35d-7a55-4ff7-9708-2001a483dfb2";

    private ObjectMapper mapper;
    private OkHttpClient client;

    public LolApi() {
        mapper = new ObjectMapper();
        client = new OkHttpClient();
    }

    public Summoner getSummonerByName(String name){
        String result = "";
        Summoner summoner;

        StringBuilder url = new StringBuilder("https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/")
                .append(name)
                .append("?api_key=" + LOL_API_KEY);

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        try{
            Response response = client.newCall(request).execute();
            result = response.body().string();
            result = result.replaceFirst("\\{", "[");
            result = result.substring(0,result.length()-1) + "]";
            String summonerString = result.substring(result.indexOf("{"), result.indexOf("}")+1);
            summoner = mapper.readValue(summonerString, Summoner.class);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return summoner;
    }

    public List<Queue> getLeagueBySummonerId(String summonerId){
        String result = "";
        List<Queue> queues = new ArrayList<>();

        StringBuilder url = new StringBuilder("https://euw.api.pvp.net/api/lol/euw/v2.5/league/by-summoner/")
                .append(summonerId)
                .append("/entry?api_key=" + LOL_API_KEY);

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        try{
            Response response = client.newCall(request).execute();
            result = response.body().string();
            String summonerString = result.substring(result.indexOf("["), result.lastIndexOf("]")+1);
            queues = mapper.readValue(summonerString, new TypeReference<List<Queue>>(){});
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return queues;
    }
}
