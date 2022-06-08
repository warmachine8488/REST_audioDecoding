package com.bharath.spring.AsseblyAITest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class RestApi {
	public static void main(String[] args) throws Exception {
		Transcript tr = new Transcript();
		tr.setAudio_url("https://github.com/warmachine8488/REST_audioDecoding/blob/main/Thirsty%20(1).mp4?raw=true");
		// tr.setId(123);
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(tr);
		System.out.println(jsonRequest);
		HttpRequest request = (HttpRequest) HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript"))
				.header("Authorization", "2a80b039d20b4b77bcecd4757558cc54").POST(BodyPublishers.ofString(jsonRequest))
				.build();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		System.out.println();
		Transcript tr2 = gson.fromJson(response.body(), Transcript.class);
		System.out.println(tr2.getId());
		HttpRequest getRequest = (HttpRequest) HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript/" + tr2.getId()))
				.header("Authorization", "2a80b039d20b4b77bcecd4757558cc54").build();

		while (true) {
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			tr2 = gson.fromJson(getResponse.body(), Transcript.class);
			System.out.println(tr2.getStatus());
			if ("completed".equals(tr2.getStatus()) || "error".equals(tr2.getStatus())) {
				break;
			}
			Thread.sleep(1000);
		}
		System.out.println("Trnscription Completed");
		System.out.println(tr2.getText());
	}

}
