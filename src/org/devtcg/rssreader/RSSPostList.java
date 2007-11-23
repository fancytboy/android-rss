/*
 * $Id$
 */

package org.devtcg.rssreader;

import org.devtcg.rssprovider.RSSReader;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ContentURI;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RSSPostList extends ListActivity
{
	private static final String[] PROJECTION = new String[] {
		RSSReader.Posts._ID, RSSReader.Posts.TITLE, RSSReader.Posts.READ };
	
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		mCursor = managedQuery(getIntent().getData(), PROJECTION, null, null);
		
		ListAdapter adapter = new RSSPostListAdapter(mCursor, this);
		
//        ListAdapter adapter = new SimpleCursorAdapter(this,
//                android.R.layout.simple_list_item_1, mCursor,
//                new String[] { RSSReader.Posts.TITLE }, new int[] { android.R.id.text1 });
        setListAdapter(adapter);
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
		ContentURI uri = RSSReader.Posts.CONTENT_URI.addId(getSelectionRowID());
    	String action = getIntent().getAction();
    	
    	if (action.equals(Intent.PICK_ACTION) ||
    	    action.equals(Intent.GET_CONTENT_ACTION))
    	{
    		setResult(RESULT_OK, uri.toString());
    	}
    	else
    	{
    		startActivity(new Intent(Intent.VIEW_ACTION, uri));
    	}
    }
    
    private static class RSSPostListAdapter extends CursorAdapter implements Filterable
    {
		public RSSPostListAdapter(Cursor c, Context context)
		{
			super(c, context);
		}
		
		private void setViewData(TextView view, Cursor cursor)
		{
			if (cursor.getInt(cursor.getColumnIndex(RSSReader.Posts.READ)) != 0)
				view.setTypeface(Typeface.DEFAULT);
			else
				view.setTypeface(Typeface.DEFAULT_BOLD);
				
			view.setText(cursor, cursor.getColumnIndex(RSSReader.Posts.TITLE));
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			setViewData((TextView)view, cursor);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			TextView post = new TextView(context);
			setViewData(post, cursor);
			return post;
		}
    }
}
