package com.lostgrad.reader.util;

import com.lostgrad.reader.data.RssItem;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by graeme on 02/04/14.
 *
 * this class reads RSS data
 *
 */
public class RssReader {

    private String rssUrl;

    //set the url with the constructor
    public RssReader(String rssUrl){

        this.rssUrl = rssUrl;

    }

    /**
     * getItems. This method is called to get the parsing process result
     */
    public List<RssItem> getItems() throws Exception{

        //get SAX Parser Factory object
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //create new parser instance
        SAXParser saxParser = factory.newSAXParser();

        //Sax parser handler object
        RssParseHandler handler = new RssParseHandler();
        //call the method parsing our rss feed
        saxParser.parse(rssUrl, handler);

        //return the parsing result
        return handler.getItems();


    }
}
