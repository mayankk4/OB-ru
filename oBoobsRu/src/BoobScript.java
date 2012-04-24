import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BoobScript {

	
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("The script is running ....");

		//////////////// Configuration Variables ///////////////////////
		String base_url = "http://oboobs.ru/";
		String base_url_media = "http://media.oboobs.ru/boobs/";
		////////////////////////////////////////////////////////////////
		
		Document doc1 = Jsoup.connect(base_url).get();
		Elements page_form = doc1.select("[name^=pageform]");

		int current_image_number = 0;
		int number_of_pages = Integer.parseInt(page_form.text().split("of ")[2].split(" ")[0]);

		for(int current_page = 1; current_page <= number_of_pages; current_page++){		
			try{	
				Document doc = Jsoup.connect(base_url + current_page).get();
				Elements img_divs = doc.select("[class^=dimage]");
	
				for(int i=0; i < img_divs.size(); i++){
					
					Elements current_img = img_divs.get(i).select("img");				
					String current_img_src = current_img.attr("src"); // /boobs_preview/05886.jpg
					String[] temp_img = current_img_src.split("/");				
					String current_image_id = temp_img[temp_img.length -1]; // 05886.jpg
	
					try {
						URL url = new URL(base_url_media + current_image_id);
	
		                InputStream in = url.openStream();
		                OutputStream out = new BufferedOutputStream(new FileOutputStream(String.format("./images/%d.jpg", current_image_number)));
		                for (int b; (b = in.read()) != -1; ) {
		                    out.write(b);
		                }
		                out.close();
		                in.close();
	
					} catch (Exception e) {
						System.out.println("Image not downloaded. Details : Image id : " + current_image_id );
						System.out.println("Continuing anyway ...");
						continue;
					}
	
	                current_image_number++;	
				}
			}catch(Exception ex){
				System.out.println("Page timed out. Details : Page number : " + current_page );
				System.out.println("Continuing anyway ...");
				continue;
			}

		} // end for
	
		System.out.println("The script is complete.");
	} // end main

}