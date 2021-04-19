package com.example.retrofit2stub;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    String API_URL = "https://pixabay.com/";
    String key = "21225042-eaf922ecbdf2c29c058db6333";
    String image_type = "all";
    TextView textViewSearch;
    ListView listView;
    ImageListAdapter imageAdapter;
    Spinner spinner;
    // TODO: реализовать скачивание и отображение картинок, найденных по запросу (Picasso)
    // TODO: добавить возможность выбора типа картинки (image_type)

    interface PixabayAPI {
        @GET("/api") // метод запроса (POST/GET) и путь к API
        // пример содержимого веб-формы q=dogs+and+people&key=MYKEY&image_type=photo
        Call<Response> search(@Query("q") String q, @Query("key") String key, @Query("image_type") String image_type);
        // Тип ответа, действие, содержание запроса

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSearch = findViewById(R.id.textSearch);
        listView = findViewById(R.id.listView);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                image_type = spinner.getSelectedItem().toString();
                startSearch(textViewSearch.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void startSearch(String text) {
        // вызывается, когда пользователь вводит текст и нажимает кнопку поиска

        // создаём экземпляр службы для обращения к API
        // можно использовать экземпляр для нескольких API сразу
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL) // адрес API сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // создаём обработчик, определённый интерфейсом PixabayAPI выше
        PixabayAPI api = retrofit.create(PixabayAPI.class);

        // указываем, какую функцию API будем использовать

        Call<Response> call = api.search(text, key, image_type);  // создали запрос

        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                // класс Response содердит поля, в которые будут записаны
                // результаты поиска по картинкам
                Response r = response.body(); // получили ответ в виде объекта
                displayResults(r.hits);
                Toast toast = Toast.makeText(getApplicationContext(), "Найдено: " + r.hits.length + " картинок", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                // обрабатываем ошибку, если она возникла
                Toast toast = Toast.makeText(getApplicationContext(), "Ошибка: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }; // обработка ответа
        call.enqueue(callback); // ставим запрос в очередь
    }

    public void displayResults(Hit[] hits) {
        imageAdapter = new ImageListAdapter(getApplicationContext(),hits);
        listView.setAdapter(imageAdapter);
    }

    public void onSearchClick(View v) {
        image_type = spinner.getSelectedItem().toString();
        startSearch(textViewSearch.getText().toString());
    }
}
