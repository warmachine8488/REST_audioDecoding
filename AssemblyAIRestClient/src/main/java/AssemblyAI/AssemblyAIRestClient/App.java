package AssemblyAI.AssemblyAIRestClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

import AssemblyAI.AssemblyAIRestClient.entity.*;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		Transcript tr = new Transcript();
		tr.setAudio_url("https://bit.ly/3yxKEIY");

		Gson gson = new Gson();
		String jsonRequest = gson.toJson(tr);

		HttpRequest postRequest = HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript"))
				.header("Authorization", Constants.API_KEY)
				.POST(BodyPublishers.ofString(jsonRequest))
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());

		System.out.println("postResponse = " + postResponse.body());
		tr = gson.fromJson(postResponse.body(), Transcript.class);

		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript/" + tr.getId()))
				.header("Authorization", Constants.API_KEY)
				.GET()
				.build();
		int counter=1;
		while (true) {
			HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
			tr = gson.fromJson(getResponse.body(), Transcript.class);
			if (tr.getStatus().equalsIgnoreCase("completed") || tr.getStatus().equalsIgnoreCase("error")) {
				break;
			}
			System.out.println("Processing...."+counter++);
		 Thread.sleep(1000);
		}
		System.out.println("Transcription Complete");
		System.out.println("Transcripted message is : " + tr.getText());
	}
}
