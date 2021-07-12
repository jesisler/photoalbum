package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import photoalbum.PhotoAlbum;

public class Main {
	
	public static void main(String[] args) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		PhotoAlbum photoAlbum = new PhotoAlbum();
		OkHttpClient okHttpClient = new OkHttpClient();
		photoAlbum.retrievePhotos(reader, okHttpClient);
	}

}
