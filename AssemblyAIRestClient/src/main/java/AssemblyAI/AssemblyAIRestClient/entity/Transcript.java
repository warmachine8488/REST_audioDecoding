package AssemblyAI.AssemblyAIRestClient.entity;

import lombok.Data;

@Data
public class Transcript {

	private String audio_url;
	private String id;
	private String status;
	private String text;
}
