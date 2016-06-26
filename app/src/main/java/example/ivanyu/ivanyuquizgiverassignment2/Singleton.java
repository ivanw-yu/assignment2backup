package example.ivanyu.ivanyuquizgiverassignment2;

import android.support.v7.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import android.content.Context;


/**
 * This class follows the Singleton design pattern and so,
 * only 1 object of this class can be instantiated. The purpose of this class is
 * to allow the creation of only 1 RequestQueue for the program to use.
 *
 *  @author Ivan Yu
 *  @version 1.0
 *  @since 2016-06-22
 */
public class Singleton extends AppCompatActivity{

    /**
     * Only 1 instance of the Singleton class and a RequstQueue will be made throughout the program.
      */
    private static Singleton myInstance;
    private static RequestQueue requestQueue;

    /**
     * This constructor instantiates the single requestQueue to be used by the entire program.
     * @param context - This parameter represents the state of an application or object.
     */
    private Singleton(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * This method creates a single instance of the Singleton object class.
     * This will be used to instantiate the Singleton object in the IvanYuQuizGiver class,
     * which will be used to add requests to the RequestQueue.
     *
     * @param context - This parameter represents the state of an application or an object.
     * @return The one and only Singleton object instantiated from this class is returned.
     */
    public static Singleton getInstance(Context context){
        // if the Singleton variable myInstance hasn't been instantiated, it will be instantiated before it's returned by this method.
        if(myInstance==null){
            myInstance = new Singleton(context);
        }
        return myInstance;
    }

    /**
     * This method will provide the only way to add requests to the private requestQueue of this class.
     *
     * @param req - This parameter is a Request object. Subclasses of Request such as JsonObjectRequest can be passed as argument
     *              for this method.
     * @param <T> - This parameter allows generic type to be used with the Request argument.
     */
    public <T> void addRequest(Request<T> req){
        requestQueue.add(req);
    }

}
