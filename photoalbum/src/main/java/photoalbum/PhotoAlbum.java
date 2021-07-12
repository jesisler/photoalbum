package photoalbum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhotoAlbum {

	private static final String photoUrlHost = "jsonplaceholder.typicode.com";
	private static final String photoUrlPath = "photos";
	private static final String queryParam = "albumId";
	private static final String scheme = "https";

	public void retrievePhotos(BufferedReader reader,OkHttpClient okHttpClient) {
		Integer albumId = null;
		System.out.println("Enter the Id number of the desired photo album, or a number less than one to exit : ");
		while (albumId == null || albumId.intValue() > 0) {
			try {
				albumId = Integer.valueOf(reader.readLine().trim());
				if(albumId <1) {
					break;
				}
				makeRestCall(albumId,okHttpClient);

			} catch (IOException | NumberFormatException e) {
				albumId = null;
				System.out.println("Exception reading albumId, please try again");
			}
		}
	}
	
	private void makeRestCall(Integer albumId,OkHttpClient okHttpClient) {
		HttpUrl url = new HttpUrl.Builder().scheme(scheme).host(photoUrlHost).addPathSegment(photoUrlPath)
				.addQueryParameter(queryParam, albumId.toString()).build();
		Request request = new Request.Builder().url(url).build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			String responseString = response.body().string();
			if (response.code() != 200) {
				System.out.println("Non 200 status response for provided album id, please try again");
			} else {
				Gson gson = new Gson();
				Type photoListType = new TypeToken<ArrayList<Photo>>() {
				}.getType();
				ArrayList<Photo> photos = gson.fromJson(responseString, photoListType);
				for (Photo p : photos) {
					StringBuilder stringBuilder = new StringBuilder().append(p.getId()).append(" ")
							.append(p.getTitle());
					System.out.println(stringBuilder.toString());
				}
			}
		} catch (Exception e) {
			System.out.println("Error deserializing JSON, please try again");
		}
	}

}
