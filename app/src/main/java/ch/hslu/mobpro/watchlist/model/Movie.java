package ch.hslu.mobpro.watchlist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "year")
    private String year;
    @ColumnInfo(name = "rated")
    private String rated;
    @ColumnInfo(name = "released")
    private String released;
    @ColumnInfo(name = "runtime")
    private String runtime;
    @ColumnInfo(name = "genre")
    private String genre;
    @ColumnInfo(name = "director")
    private String director;
    @ColumnInfo(name = "writer")
    private String writer;
    @ColumnInfo(name = "actors")
    private String actors;
    @ColumnInfo(name = "plot")
    private String plot;
    @ColumnInfo(name = "language")
    private String language;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "awards")
    private String awards;
    @ColumnInfo(name = "poster")
    private String poster;
    @ColumnInfo(name = "metascore")
    private String metascore;
    @ColumnInfo(name = "imdbRating")
    private String imdbRating;
    @ColumnInfo(name = "imdbVotes")
    private String imdbVotes;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "totalSeasons")
    private String totalSeasons;
    @ColumnInfo(name = "dvd")
    private String dvd;
    @ColumnInfo(name = "boxOffice")
    private String boxOffice;
    @ColumnInfo(name = "production")
    private String production;
    @ColumnInfo(name = "website")
    private String website;
    @ColumnInfo(name = "watchlist")
    private Boolean isWatchlist;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTotalSeasons(String totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public void setDvd(String dvd) {
        this.dvd = dvd;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getPoster() {
        return poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getType() {
        return type;
    }

    public String getTotalSeasons() {
        return totalSeasons;
    }

    public String getDvd() {
        return dvd;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getProduction() {
        return production;
    }

    public String getWebsite() {
        return website;
    }

    public Boolean getWatchlist() {
        return isWatchlist;
    }

    public void setWatchlist(Boolean watchlist) {
        isWatchlist = watchlist;
    }
}
