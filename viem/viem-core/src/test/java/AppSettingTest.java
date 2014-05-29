import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import de.anhquan.viem.core.model.AppSetting;
import de.anhquan.viem.core.util.AppSettingImporter;
import de.anhquan.viem.core.util.ParseSettingException;


public class AppSettingTest {

	@Test
	public void testImportSetting() throws FileNotFoundException, ParseSettingException{
		
		AppSettingImporter importer = new AppSettingImporter();
		
		String content = new Scanner(new File("settings.demo.json")).useDelimiter("\\Z").next();
		System.out.println(content);
		List<AppSetting> settings = importer.parse(content);
		for (AppSetting appSetting : settings) {
			System.out.println(appSetting);
		}
	}

}
