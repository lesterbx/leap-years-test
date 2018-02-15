package leapyears;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import java.util.GregorianCalendar;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
/**
 *
 * @author Lester Pérez
 */
public class LeapYears {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        Path inputPath = Paths.get(args[0]);
        Path outputPath = Paths.get(args[1]);
        
        //Check if the year is correct (4 digits per line)
        Predicate<String> isValidYear = year -> year.matches("^\\d{4}$");
        
        //Return a string saying if is a leap year
        Function<String, String> processYear = year -> 
            new GregorianCalendar().isLeapYear(Integer.parseInt(year)) ? 
                year+";Bisiesto" : 
                year+";NO Bisiesto";
        
        try {
            //Read the input file, remove wrong lines, sort and process the years
            Stream<String> processedYears = Files.lines(inputPath)
                .filter(isValidYear).sorted().map(processYear);
            
            //Write the processed years to the output file
            Path output = Files.write(
                outputPath,
                (Iterable<String>) processedYears::iterator,
                CREATE_NEW
            );
            
            System.out.println("Result written to " + output);
        } catch (IOException ex) {
            if(ex instanceof NoSuchFileException){
                //Input file doesn't exists
                System.out.println("File doesn't exist: " + ex.getMessage());
            }else if(ex instanceof FileAlreadyExistsException){
                //Output file already exists
                System.out.println("File already exist: " + ex.getMessage());
            }
        }
    }    
}
