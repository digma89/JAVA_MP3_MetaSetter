package main;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class BackEnd {
	
	//Method that loops in directory and get files names
	public void getFiles(String folerSource, String folderResult, String album, String year, String genre) throws BaseException, IOException, Exception{
		File folder = new File(folerSource);
		File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	 
		     	//send file name && folder result to next method			        
		        setMetaData(listOfFiles[i].getName().trim(), folerSource, folderResult, album, year,genre);	
		      }
		    }
	}
	
	//With the file name set the MetaData of the files
	private void setMetaData(String fileName, String folerSource, String folderResult, String album, String year, String genre) throws BaseException, Exception, IOException{		
        if(fileName.endsWith(".mp3") && fileName != "" && !fileName.equals(null)){
	        
        	//Local variables
        	String artistName;
	        String title;
	        
	        artistName = fileName.substring(0, fileName.indexOf("-")).trim();
	        title = fileName.substring(fileName.indexOf("-")+1,fileName.indexOf(".mp3") ).trim();	 
	        
	        //Open mp3 file
	        Mp3File mp3file = new Mp3File(folerSource +fileName);	        
			if (mp3file.hasId3v2Tag()) {
				 ID3v2 id3v2Tag = mp3file.getId3v2Tag();			 
				 id3v2Tag.setArtist(artistName);	
				 id3v2Tag.setTitle(title);				
				 if(!album.isEmpty()){
					 System.out.println("album:" +album);
					 id3v2Tag.setAlbum(album);	
				 }
				 if(!year.isEmpty()){
					 id3v2Tag.setYear(year);	
				 }
				 if(!genre.isEmpty()){
					 id3v2Tag.setGenreDescription(genre);	
				 }
				 mp3file.save(folderResult +fileName);
				 System.out.println("Complete for: " + fileName);
			}               
        }
	}

}
