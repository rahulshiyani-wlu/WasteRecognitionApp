package com.example.project.waste_recognition_app;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.RequestBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30,  application = TestApplication.class)
public class ItemUnitTesting {

    @Mock
    private Context mockContext;

    @Mock
    private RequestBuilder mockRequestBuilder;

    private Item.ItemListAdapter adapter;
    private List<Item> mockItems;
    private LayoutInflater mockLayoutInflater;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Glide's RequestManager and RequestBuilder
        RequestManager mockRequestManager = mock(RequestManager.class);
        RequestBuilder mockRequestBuilder = mock(RequestBuilder.class);

        // Mock Glide.with to return the mocked RequestManager
        when(Glide.with(any(Context.class))).thenReturn(mockRequestManager);
        when(Glide.with(any(View.class))).thenReturn(mockRequestManager);

        // Mock load and asGif to return a RequestBuilder
        when(mockRequestManager.load(anyString())).thenReturn(mockRequestBuilder);
        when(mockRequestManager.asGif()).thenReturn(mockRequestBuilder);

        // Create mock items
        mockItems = new ArrayList<>();
        mockItems.add(new Item("1", "Recyclable", "https://example.com/image1.jpg", "2023-12-01", "10:00 AM"));
        mockItems.add(new Item("2", "Organic", "https://example.com/image2.jpg", "2023-12-02", "11:00 AM"));

        // Initialize the adapter
        adapter = new Item.ItemListAdapter(mockContext, mockItems);
    }

    @Test
    public void testUpdateList_UpdatesDataAndNotifiesAdapter() {
        // Create a new list of items
        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("3", "Plastic", "https://example.com/image3.jpg", "2023-12-03", "12:00 PM"));

        // Update the adapter's list
        adapter.updateList(newItems);

        // Verify that the list was updated and the adapter was notified
        assertEquals(1, adapter.getCount());
        assertEquals("Plastic", adapter.getItem(0).getType());
    }
}