package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Preparation {
	public static void clean_up()
	{
		String files_to_delete[] = {"moquette_store.mapdb", "moquette_store.mapdb.p", "moquette_store.mapdb.t"};
		
		for(String fileName : files_to_delete)
		{
			File file = new File(fileName);
			try
			{
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
