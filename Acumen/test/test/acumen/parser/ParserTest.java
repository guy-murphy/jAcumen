package test.acumen.parser;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.*;

import acumen.map.model.IOccurence;
import acumen.map.model.ITopic;
import acumen.map.store.MySqlTopicStore;
import acumen.data.StoreException;

import acumen.parser.scanner.*;
import acumen.resource.model.Resource;
import acumen.resource.store.MySqlFileStore;
import acumen.util.AcumenDictionary;
import acumen.util.Is;

public class ParserTest {
	
	@Test
	public void tokenizer () throws FileNotFoundException, IOException, ParseException {
		File file = new File("/home/guy/workspace/Acumen/test/test/acumen/parser/wiki.txt");
		FileInputStream stream = new FileInputStream(file);
		try {
			boolean xmlOutput = true;
			WikiParser parser = new WikiParser(stream, "UTF8");
			if (xmlOutput) {
				parser.wiki();
				parser.setPrettyPrint(true);
				String xml = parser.toXml();
				System.out.println(xml);
			} else {
				Token token = null;
				do {
					token = parser.getNextToken();
					System.out.println(String.format("%s: %s", token.kind, token.image));
				} while (token.kind != 0);
			}
		} finally {
			stream.close();
		}
		
	}
	
	@Test
	public void consumeString () throws ParseException, IOException {
		WikiParser parser = new WikiParser("to be or not to be that is the question.\n\n");
		parser.wiki();
		parser.setPrettyPrint(true);
		System.out.println(parser.toXml());
	}
	
	@Test
	public void bulkTestWikiParse () throws ParseException, IOException, StoreException {
		MySqlTopicStore topicStore = new MySqlTopicStore("thejudge.acumen.es", "acumen", "root", "fusax329");
		MySqlFileStore fileStore = new MySqlFileStore("thejudge.acumen.es", "acumen", "root", "fusax329");
		fileStore.setNamespace("DevWiki");
		try {
			topicStore.start();
			fileStore.start();
			AcumenDictionary<String, String> failures = new AcumenDictionary<String, String>();
			String[] topicIds = topicStore.getTopicIds();
			WikiParser parser;
			for (int i = 0; i < topicIds.length; i++) {
				String topicId = topicIds[i];
				ITopic topic = topicStore.getTopic(topicId);
				if (topic != null) {
					for (IOccurence occurence: topic.getOccurences()) {
						System.out.println("#######");
						System.out.println(String.format("\n#> Processing '%s > %s'...", topicId, occurence.getReference()));
						Resource resource = fileStore.getMostRecentInstance(occurence.getReference());
						if (resource != null) {
							String wiki = resource.getStringData();
							if (Is.NotNullOrEmpty(wiki)) {
								parser = new WikiParser(wiki);
								try {
									parser.wiki();
									parser.setPrettyPrint(true);
									try {
										String result = parser.toXml();
										if (result.equals("<wiki/>")) {
											fail("The parse produced '<wiki/>'");
										}
									} catch (Exception err) {
										System.out.println(String.format("#> The resource '%s > %s' failed to format into XML.", topicId, occurence.getReference()));
										System.out.println(String.format("#> %s", err.getMessage()));
										System.out.println(wiki);
										failures.put(topicId, err.getMessage());
									}
								} catch (Throwable err) {
									System.out.println(String.format("#> The resource '%s > %s' failed to parse.", topicId, occurence.getReference()));
									System.out.println(String.format("#> %s", err.getMessage()));
									System.out.println(wiki);
									failures.put(topicId, err.getMessage());
								}
							} else {
								System.out.println(String.format("#> The resource '%s' had no wiki.", occurence.getReference()));
							}
						} else {
							System.out.println(String.format("#> The resource '%s' could not be found.", occurence.getReference()));
						}
						System.out.println("#######");
					}
				} else {
					System.out.println(String.format("#> The topic '%s' could not be found.", (topicIds.length - failures.size())));
				}
			}
			
			System.out.println(String.format("\n#> There were %s passes.", (topicIds.length - failures.size())));
			System.out.println(String.format("\n#> There were %s failures.", failures.size()));
			
			if (failures.size() > 0) {
				fail(String.format("There were %s failures.", failures.size()));
			}
		} finally {
			topicStore.stop();
			fileStore.stop();
		}
	}

}
