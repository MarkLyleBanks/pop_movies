package com.marklylebanks.popularmovies;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

       public  List movies = new ArrayList<MutableLiveData<Movie>>();

    public MainViewModel() {
        super();
       }

public List<Movie> getMovies() {
        return  movies;
}

public void loadData (List<Movie> movieList){
        movies = movieList;
}



}

