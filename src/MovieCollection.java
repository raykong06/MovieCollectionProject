import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieCollection
{
    private ArrayList<Movie> movies;
    private ArrayList<String> fullCast;
    private Scanner scanner;

    public MovieCollection(String fileName)
    {
        importMovieList(fileName);
        castMembers();
        scanner = new Scanner(System.in);
    }

    public ArrayList<Movie> getMovies()
    {
        return movies;
    }

    public void menu()
    {
        String menuOption = "";

        System.out.println("Welcome to the movie collection!");
        System.out.println("Total: " + movies.size() + " movies");

        while (!menuOption.equals("q"))
        {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (k)eywords");
            System.out.println("- search (c)ast");
            System.out.println("- see all movies of a (g)enre");
            System.out.println("- list top 50 (r)ated movies");
            System.out.println("- list top 50 (h)igest revenue movies");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();

            if (!menuOption.equals("q"))
            {
                processOption(menuOption);
            }
        }
    }

    private void processOption(String option)
    {
        if (option.equals("t"))
        {
            searchTitles();
        }
        else if (option.equals("c"))
        {
            searchCast();
        }
        else if (option.equals("k"))
        {
            searchKeywords();
        }
        else if (option.equals("g"))
        {
            listGenres();
        }
        else if (option.equals("r"))
        {
            listHighestRated();
        }
        else if (option.equals("h"))
        {
            listHighestRevenue();
        }
        else
        {
            System.out.println("Invalid choice!");
        }
    }

    private void searchTitles()
    {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine();

        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();

        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();

        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String movieTitle = movies.get(i).getTitle();
            movieTitle = movieTitle.toLowerCase();

            if (movieTitle.indexOf(searchTerm) != -1)
            {
                //add the Movie objest to the results list
                results.add(movies.get(i));
            }
        }

        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void sortResults(ArrayList<Movie> listToSort)
    {
        for (int j = 1; j < listToSort.size(); j++)
        {
            Movie temp = listToSort.get(j);
            String tempTitle = temp.getTitle();

            int possibleIndex = j;
            while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }

    private void sortCastResults(ArrayList<String> listToSort)
    {
        for (int i = 1; i < listToSort.size(); i++)
        {
            String temp = listToSort.get(i);
            int possibleIndex = i;
            while (possibleIndex > 0 && temp.compareTo(listToSort.get(possibleIndex - 1)) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }

    private void displayMovieInfo(Movie movie)
    {
        System.out.println();
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Runtime: " + movie.getRuntime() + " minutes");
        System.out.println("Year: " + movie.getYear());
        System.out.println("Directed by: " + movie.getDirector());
        System.out.println("Cast: " + movie.getCast());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("User rating: " + movie.getUserRating());
        System.out.println("Box office revenue: " + movie.getRevenue());
    }

    private void searchCast()
    {
        System.out.print("Enter an actor to search for: ");
        String searchCast = scanner.nextLine();

        searchCast = searchCast.toLowerCase();

        ArrayList<String> castResults = new ArrayList<String>();

        for (int i = 0; i < fullCast.size(); i++)
        {
            String specificCast = fullCast.get(i);
            String specificCastLowerCase = specificCast.toLowerCase();
            if (specificCastLowerCase.indexOf(searchCast) > -1)
            {
                castResults.add(specificCast);
            }
        }

        sortCastResults(castResults);

        for (int i = 0; i < castResults.size(); i++)
        {
            String actor = castResults.get(i);

            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + actor);
        }

        System.out.println("For which actor would you like to see what movies they have been casted in?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        String selectedActor = castResults.get(choice - 1);

        ArrayList<Movie> results = new ArrayList<Movie>();

        for (int i = 0; i < movies.size(); i++)
        {
            String cast[] = movies.get(i).getCast().split("\\|");
            for (int j = 0; j < cast.length; j++)
            {
                String actor = cast[j].toLowerCase();
                if (actor.equals(selectedActor.toLowerCase()))
                {
                    results.add(movies.get(i));
                    j = cast.length;
                }
            }
        }

        learnMoreAbout(results);
    }

    private void searchKeywords()
    {
        System.out.print("Enter a keyword to search for: ");
        String searchKeyword = scanner.nextLine();

        searchKeyword = searchKeyword.toLowerCase();

        ArrayList<Movie> results = new ArrayList<Movie>();

        for (int i = 0; i < movies.size(); i++)
        {
            String extractKeyword = movies.get(i).getKeywords();
            String[] movieKeywords = extractKeyword.split("\\|");
            for (int k = 0; k < movieKeywords.length; k++)
            {
                String keyword = movieKeywords[k].toLowerCase();
                if (keyword.indexOf(searchKeyword) > -1)
                {
                    results.add(movies.get(i));
                    k = movieKeywords.length;
                }
            }
        }

        /*
        sortResults(results);

        for (int j = 0; j < results.size(); j++)
        {
            String title = results.get(j).getTitle();

            int choiceNum = j + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();

         */
        learnMoreAbout(results);
    }

    private void listGenres()
    {

    }

    private void listHighestRated()
    {

    }

    private void listHighestRevenue()
    {

    }

    private void learnMoreAbout(ArrayList<Movie> results)
    {
        sortResults(results);

        for (int j = 0; j < results.size(); j++)
        {
            String title = results.get(j).getTitle();

            int choiceNum = j + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void castMembers()
    {
        fullCast = new ArrayList<String>();
        for (int i = 0; i < movies.size(); i++)
        {
            String[] movieCast = movies.get(i).getCast().split("\\|");
            for (int j = 0; j < movieCast.length; j++)
            {
                String actor = movieCast[j];
                String actorLowerCase = actor.toLowerCase();
                boolean inList = false;
                for (int k = 0; k < fullCast.size(); k++)
                {
                    if (actorLowerCase.equals(fullCast.get(k).toLowerCase()))
                    {
                        inList = true;
                    }
                }
                if (!inList)
                {
                    fullCast.add(actor);
                }
            }
        }
    }

    private void importMovieList(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            movies = new ArrayList<Movie>();

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] movieFromCSV = line.split(",");

                String title = movieFromCSV[0];
                String cast = movieFromCSV[1];
                String director = movieFromCSV[2];
                String tagline = movieFromCSV[3];
                String keywords = movieFromCSV[4];
                String overview = movieFromCSV[5];
                int runtime = Integer.parseInt(movieFromCSV[6]);
                String genres = movieFromCSV[7];
                double userRating = Double.parseDouble(movieFromCSV[8]);
                int year = Integer.parseInt(movieFromCSV[9]);
                int revenue = Integer.parseInt(movieFromCSV[10]);

                Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
                movies.add(nextMovie);
            }
            bufferedReader.close();
        }
        catch(IOException exception)
        {
            // Print out the exception that occurred
            System.out.println("Unable to access " + exception.getMessage());
        }
    }
}