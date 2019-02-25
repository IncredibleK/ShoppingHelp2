package hu.bme.aut.shoppinghelper.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.DataListener;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;
import hu.bme.aut.shoppinghelper.data.ShoppingList;
import hu.bme.aut.shoppinghelper.fragments.CategoryFragment;
import hu.bme.aut.shoppinghelper.fragments.ListFragment;
import hu.bme.aut.shoppinghelper.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    private static List<ShoppingList> shoppingLists;
    private static List<Category> categories;
    private static List<DataListener> shoppingListListeners;
    private static List<DataListener> categorytListeners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListListeners = new ArrayList<DataListener>();
        categorytListeners = new ArrayList<DataListener>();
        initFragmentManager();

        setContentView(R.layout.activity_main);
        //      Snackbar.make(findViewById(R.id.content), "Item "+item.getName(), Snackbar.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingList item = ShoppingList.findById(ShoppingList.class, 1L);
                Snackbar.make(view, "Replace with your own action " + item.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        invalidateOptionsMenu();
    }

    private void initFragmentManager(){
        fragmentManager = getSupportFragmentManager();
        ListFragment listFragment = ListFragment.newInstance(getApplicationContext());
        shoppingListListeners.add(listFragment);
        fragmentManager.beginTransaction().replace(R.id.content_main, listFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            createSettingsFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createSettingsFragment(){
        fragmentManager = getSupportFragmentManager();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentManager.beginTransaction().replace(R.id.content_main, settingsFragment).addToBackStack(null).commit();
    }


    private boolean existingListName(String name) {
        List<ShoppingList> allLists = ShoppingList.listAll(ShoppingList.class);
        for (ShoppingList list : allLists) {
            if (list.getName().equals(name))
                return true;
        }
        return false;
    }

    private boolean existingCategoryName(String name) {
        List<Category> allLists = Category.listAll(Category.class);
        for (Category category : allLists) {
            if (category.getName().equals(name))
                return true;
        }
        return false;
    }

    public static void sendListNotification() {
        for (DataListener listener : shoppingListListeners)
            listener.update();
    }

    public static void sendCategoryNotification() {
        for (DataListener listener : categorytListeners)
            listener.update();
    }

    private boolean validate(String string) {
        if (string.length() < 3 || string.length() > 21)
            return false;
        return true;
    }

    private void createListAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_list, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(view);
        alert.setTitle("Create a shopping list");

        final EditText editText = view.findViewById(R.id.et_createlist);

        alert.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String listName = editText.getText().toString();

                if (validate(listName)) {
                    if (existingListName(listName) != true) {
                        ShoppingList list = new ShoppingList(listName);
                        list.save();
                        sendListNotification();
                    } else
                        Snackbar.make(findViewById(R.id.content_main), "This list name already exists!", Snackbar.LENGTH_LONG).show();
                } else
                    Snackbar.make(findViewById(R.id.content_main), "List name must be between 3 and 20 characters!", Snackbar.LENGTH_LONG).show();

            }
        });

        alert.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void createCategoryAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_category, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(view);
        alert.setTitle("Create a category");

        final EditText editText = view.findViewById(R.id.et_createcategory);
        alert.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String categoryName = editText.getText().toString();

                if (validate(categoryName)) {
                    if (existingCategoryName(categoryName) != true) {
                        Category category = new Category(categoryName);
                        category.save();
                        sendCategoryNotification();
                    } else
                        Snackbar.make(findViewById(R.id.content_main), "This category name already exists!", Snackbar.LENGTH_LONG).show();
                } else
                    Snackbar.make(findViewById(R.id.content_main), "Category name must be between 3 and 20 characters!", Snackbar.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void createCategoryFragment(){
        fragmentManager = getSupportFragmentManager();
        CategoryFragment categoryFragment = CategoryFragment.newInstance(getApplicationContext());
        categorytListeners.add(categoryFragment);
        fragmentManager.beginTransaction().replace(R.id.content_main, categoryFragment).addToBackStack(null).commit();
    }

    private void createShoppingListFragment(){
        fragmentManager = getSupportFragmentManager();
        ListFragment listFragment = ListFragment.newInstance(getApplicationContext());
        shoppingListListeners.add(listFragment);
        fragmentManager.beginTransaction().replace(R.id.content_main, listFragment).addToBackStack(null).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_createlist) {
            createListAlertDialog();
        } else if (id == R.id.nav_createcategory) {
            createCategoryAlertDialog();
        } else if (id == R.id.nav_categories) {
            createCategoryFragment();
        } else if (id == R.id.nav_shoppinglists) {
            createShoppingListFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
