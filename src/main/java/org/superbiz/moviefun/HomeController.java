package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {


    private PlatformTransactionManager albumsPlatformTransactionManager;
    private PlatformTransactionManager moviesPlatformTransactionManager;

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;

    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures,PlatformTransactionManager albumsPlatformTransactionManager,PlatformTransactionManager moviesPlatformTransactionManager) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.albumsPlatformTransactionManager=albumsPlatformTransactionManager;
        this.moviesPlatformTransactionManager=moviesPlatformTransactionManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        addMovies();
        addAlbums();



        model.put("albums", albumsBean.getAlbums());
        model.put("movies", moviesBean.getMovies());

        return "setup";
    }
    /*public void addMovie()
    {

            TransactionTemplate transactionTemplate=new TransactionTemplate(moviesPlatformTransactionManager);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    for (Movie movie : movieFixtures.load()) {
                    moviesBean.addMovie(movie);
                    }
                }
            });
    }
    */

    private void addMovies(){
        new TransactionTemplate(moviesPlatformTransactionManager).execute(transactionStatus -> {
            for (Movie movie : movieFixtures.load()) {
                moviesBean.addMovie(movie);
            }
            return null;
        });
    }

    private void addAlbums(){
        new TransactionTemplate(albumsPlatformTransactionManager).execute(transactionStatus -> {
            for (Album album : albumFixtures.load()) {
                albumsBean.addAlbum(album);
            }
            return null;
        });
    }

    /*public void addAlbum()
    {

            TransactionTemplate transactionTemplate=new TransactionTemplate(albumsPlatformTransactionManager);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    for (Album album : albumFixtures.load()) {
                    albumsBean.addAlbum(album);
                }
                }
            });


    }*/
}
