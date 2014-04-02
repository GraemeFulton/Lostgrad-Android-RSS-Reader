package com.lostgrad.reader.util;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;
import com.lostgrad.reader.data.RssItem;


/**
 * Created by graeme on 02/04/14.
 */
public class RssParseHandler extends DefaultHandler {

    private List<RssItem> rssItems;

    private StringBuffer rssTitle;
    //reference to current item whilst parsing
    private RssItem currentItem;

    //parsing title indicator
    private boolean parsingTitle;

    //parsing link indicator
    private boolean parsingLink;

    public RssParseHandler() {
        rssItems = new ArrayList();
    }
    // We have an access method which returns a list of items that are read from the RSS feed. This method will be called when parsing is done.
    public List<RssItem> getItems() {
        return rssItems;
    }


    // The StartElement method creates an empty RssItem object when an item start tag is being processed.
    // When a title or link tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("item".equals(qName)){
            currentItem=new RssItem();
        }else if ("title".equals(qName)){
            rssTitle = new StringBuffer();
            parsingTitle=true;
        }else if ("link".equals(qName)){
            parsingLink=true;
        }
    }

    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed.
    // It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("item".equals(qName)){
            rssItems.add(currentItem);
            currentItem=null;
        } else if("title".equals(qName)){
              parsingTitle=false;
        }else if("link".equals(qName)){
              parsingLink=false;
        }
    }

    // Characters method fills current RssItem object with data when title and link tag content is being processed
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(parsingTitle){
            if(currentItem!=null){
                rssTitle.append(new String(ch, start, length));
                currentItem.setTitle(rssTitle.toString());
                //currentItem.setTitle(new String(ch,start,length));
            }
        } else if (parsingLink){
            if(currentItem!=null){
                currentItem.setLink(new String(ch,start,length));
                parsingLink=false;
            }
        }
    }
}
