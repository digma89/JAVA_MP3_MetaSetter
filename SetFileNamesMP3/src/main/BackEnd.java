package main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;

public class BackEnd {
	
	//With the file name set the MetaData of the files
	public void setMetaData(String folerSource, String folderResult, String album, String year, String genre, boolean changeTag) throws BaseException, Exception, IOException{		
       
    	//Local variables
    	String artistName;
        String title;
        String fileName;
        	
        File folder = new File(folerSource);
    	File[] listOfFiles = folder.listFiles();
    	for (int i = 0; i < listOfFiles.length; i++) {
    		if (listOfFiles[i].isFile()) {		
    			fileName = listOfFiles[i].getName().trim();
    			if(fileName.endsWith(".mp3") && fileName != "" && !fileName.equals(null)){				        
					
    				artistName = fileName.substring(0, fileName.indexOf("-")).trim();
					title = fileName.substring(fileName.indexOf("-")+1,fileName.indexOf(".mp3") ).trim();	 
				        
				    //Open mp3 file
				    Mp3File mp3file = new Mp3File(folerSource +fileName);	        
					if (mp3file.hasId3v2Tag()) {
						
						//Change tagV2.4 to V2.3 windows can't read V2.4
						if(changeTag){					
						ID3v2 id3v2Tag1 = new ID3v23Tag();
						mp3file.setId3v2Tag(id3v2Tag1);
						}
						
						ID3v2 id3v2Tag = mp3file.getId3v2Tag();							
						id3v2Tag.setArtist(artistName);	
						id3v2Tag.setTitle(title);				
						if(!album.isEmpty()){							
							id3v2Tag.setAlbum(album);	
							}
						if(!year.isEmpty()){
							id3v2Tag.setYear(year);	
						}
						if(!genre.isEmpty()){
							id3v2Tag.setGenreDescription(genre);	
						}
						mp3file.save(folderResult +fileName);				 
					}  
    		    }
    		}
        }
	}
	
	//With the file name set the MetaData of the files
	public void setFileNames(String folerSource, String folderResult) throws BaseException, Exception, IOException{		
       
    	//Local variables
        String fileName;
        String newFileName = "";
        	
        File folder = new File(folerSource);
    	File[] listOfFiles = folder.listFiles();
    	for (int i = 0; i < listOfFiles.length; i++) {
    		if (listOfFiles[i].isFile()) {		
    			fileName = listOfFiles[i].getName().trim();
    			if(fileName.endsWith(".mp3") && fileName != "" && !fileName.equals(null)){		 
    				
				    //Open mp3 file
				    Mp3File mp3file = new Mp3File(folerSource +fileName);	        
					if (mp3file.hasId3v2Tag()) {
						ID3v2 id3v2Tag = mp3file.getId3v2Tag();			 
						newFileName = id3v2Tag.getArtist() +" - " + id3v2Tag.getTitle() +".mp3";
						mp3file.save(folderResult +newFileName);				 
					}  
    		    }
    		}
        }
	}

}
