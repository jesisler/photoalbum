package photoalbum;

import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RunWith(MockitoJUnitRunner.class)
public class PhotoAlbumTest {

	@Mock
	OkHttpClient okHttpClient;

	@Mock
	BufferedReader reader;

	@Mock
	Call call;

	Request request;

	Response response;

	OutputStream outputStream;

	Photo testPhoto;

	ArrayList<Photo> photos;

	PhotoAlbum photoAlbum = new PhotoAlbum();

	@Before
	public void setUp() {
		request = new Request.Builder().url("https:test").build();
		photos = new ArrayList<Photo>();
		testPhoto = new Photo();
		testPhoto.setAlbumId(1);
		testPhoto.setId(1);
		testPhoto.setTitle("test");
		photos.add(testPhoto);
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}

	@Test
	public void testHappyPath() throws IOException {
		Gson gson = new Gson();
		Response response = new Response.Builder()
				.body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), gson.toJson(photos)))
				.code(200).request(request).protocol(Protocol.HTTP_1_0).message("message").build();
		when(reader.readLine()).thenReturn("1", "0");
		when(okHttpClient.newCall(Mockito.any())).thenReturn(call);
		when(call.execute()).thenReturn(response);
		photoAlbum.retrievePhotos(reader, okHttpClient);
		String result = outputStream.toString();
		Assert.assertTrue(result.contains("Enter the Id number of the desired photo album, or a number less than one to exit : "));
		Assert.assertTrue(result.contains("1 test"));
	}
	
	@Test
	public void testIOError() throws IOException {
		when(reader.readLine()).thenReturn("a", "0");
		photoAlbum.retrievePhotos(reader, okHttpClient);
		String result = outputStream.toString();
		Assert.assertTrue(result.contains("Enter the Id number of the desired photo album, or a number less than one to exit : "));
		Assert.assertTrue(result.contains("Exception reading albumId, please try again"));
	}
	
	@Test
	public void testFailedCall() throws IOException {
		Gson gson = new Gson();
		Response response = new Response.Builder()
				.body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), gson.toJson(photos)))
				.code(400).request(request).protocol(Protocol.HTTP_1_0).message("message").build();
		when(reader.readLine()).thenReturn("1", "0");
		when(okHttpClient.newCall(Mockito.any())).thenReturn(call);
		when(call.execute()).thenReturn(response);
		photoAlbum.retrievePhotos(reader, okHttpClient);
		String result = outputStream.toString();
		Assert.assertTrue(result.contains("Enter the Id number of the desired photo album, or a number less than one to exit : "));
		Assert.assertTrue(result.contains("Non 200 status response for provided album id, please try again"));
	}
	
	@Test
	public void testBadJSON() throws IOException {
		Response response = new Response.Builder()
				.body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), ""))
				.code(200).request(request).protocol(Protocol.HTTP_1_0).message("message").build();
		when(reader.readLine()).thenReturn("1", "0");
		when(okHttpClient.newCall(Mockito.any())).thenReturn(call);
		when(call.execute()).thenReturn(response);
		photoAlbum.retrievePhotos(reader, okHttpClient);
		String result = outputStream.toString();
		Assert.assertTrue(result.contains("Enter the Id number of the desired photo album, or a number less than one to exit : "));
		Assert.assertTrue(result.contains("Error deserializing JSON, please try again"));
	}

}
