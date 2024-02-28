// ################################################################################
// CLASS: CSC 635
// NAME: ROMAN CAMPBELL
// ASSIGNMENT: LAB 6
// ################################################################################

// ################################################################################
// IMPORTS
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Main
{
    public static void main(String[] args) 
    {
        // ################################################################################
        // VARIABLES
        int low_num = 0;
        int high_num = 0;
        int temp_num1 = 0;
        int temp_num2 = 0;
        int cell_num = 0;
        int factor_columns = 0;
        int factor_rows = 0;
        int high_num_sqrt;
        int color_hash_key = 0;
        int gray_hash_key = 0;
        int how_many_colors = 0;
        int how_many_grays = 0;
        int color_key = 0;
        int gray_key = 0;
        float scaling_factor_rainbow;
        float scaling_factor_gray;
        String line = "";
        String key_value;
        String formatted_num_rainbow;
        String formatted_num_gray;
        boolean is_gray_start = false;

        // ################################################################################
        // OBJECTS
        StringBuilder html_table = new StringBuilder();
        StringBuilder multi_table = new StringBuilder();
        StringBuilder css_content = new StringBuilder();
        HashMap<Integer, String> css_class_rainbow = new HashMap<Integer, String>();
        HashMap<Integer, String> css_class_gray = new HashMap<Integer, String>();

        // Get command line args, attempt to convert to int, warn if args contain non numeric chars
        if(args.length != 2)
        {
            System.err.println("Usage: java Main <arg1> <arg2>");
            System.exit(1);
        }

        if(args.length == 2)
        {
            // Attempt conversion, warn if failure
            try
            {
                temp_num1 = Integer.parseInt(args[0]);
                temp_num2 = Integer.parseInt(args[1]);
            } catch(NumberFormatException e)
            {
                System.err.println("Argument contains non-numeric, use numeric arguments only.");
                System.err.println("Usage: java Main <arg1> <arg2>");
                System.exit(1);
            }

            // Swap if the second arg is smaller
            
            if(temp_num1 < temp_num2)
            {
                low_num = temp_num1;
                high_num = temp_num2;
            }

            if(temp_num1 > temp_num2)
            {
                low_num = temp_num2;
                high_num = temp_num1;
            }
        }

        // ################################################################################
        // Load CSS file then put contents into hashmap
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("../css/stylesheets.css"));

            while((line = br.readLine()) != null)
            {
                css_content.append(line).append("\n");
                String[] line_parts = line.split("[{}]");

                // Finding only the class names and putting them into hashmap with integer keys
                // to be used for future calculations and which colors to use
                // Dynamically separate gray key from color key
                for( String name : line_parts)
                {
                    if(name.contains("GRAY SCALE STARTS HERE"))
                    {
                        is_gray_start = true;
                        continue;
                    }
                    // Separate grays from rainbow
                    if(name.startsWith("."))
                    {
                        name = name.substring(1).trim(); // Remove . so I can embed <td class="color">

                        // Rainbow colors
                        if(!is_gray_start)
                        {
                            css_class_rainbow.put(color_hash_key, name);
                            color_hash_key++;
                        }
                     
                        // Gray scale
                        if(is_gray_start)
                        {
                            css_class_gray.put(gray_hash_key, name);
                            gray_hash_key++;
                        }
                       
                    }
                }
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        // ################################################################################
        // Compute scaling factor 
        how_many_colors = css_class_rainbow.size();
        how_many_grays = css_class_gray.size();

        scaling_factor_rainbow = high_num - low_num;
        scaling_factor_gray = high_num - low_num;

        scaling_factor_rainbow = how_many_colors / scaling_factor_rainbow;
        scaling_factor_gray = how_many_grays / scaling_factor_gray;

        // Round to two decimal places
        formatted_num_rainbow = String.format("%.2f", scaling_factor_rainbow);
        scaling_factor_rainbow = Float.parseFloat(formatted_num_rainbow);

        formatted_num_gray = String.format("%.2f", scaling_factor_gray);
        scaling_factor_gray = Float.parseFloat(formatted_num_gray);

        // ################################################################################
        // Dynamically create multiple table section
        
        // Find the square root to calulate header numbers needed in multiplicatin table
        high_num_sqrt = (int)Math.ceil(Math.sqrt(high_num));

        // Need to find the correct factors for the given high_num
        for(int i = 2; i <= high_num_sqrt; i++)
        {
            if(high_num % i == 0)
            {
                factor_rows = i;
                factor_columns = high_num / i;
            }
        }
        
        // ###############################################################################
        // ###############################################################################
        // START TABLES
        // ###############################################################################
        // ###############################################################################
        // CYCLE 0 - REGULAR
        // CYCLE 1 - GRAY SCALE
        // CYCLE 2 - RAINBOWS

        // Start building multiplication table
        for(int cycle = 0; cycle <= 2; cycle++)
        {
            switch(cycle)
            {
                case 0: multi_table.append("<h3>Regular Table"); break;
                case 1: multi_table.append("<h3>Grayscale Table"); break;
                case 2: multi_table.append("<h3>Rainbow Scale Table</h3>"); break;
            }
            multi_table.append("<tr>");
            multi_table.append("<th>X</th>");
    
            // Create the multiplication horizontal table header
            for(int i = low_num; i <= factor_columns; i++)
            {
                multi_table.append("<th>").append(i).append("</th>");
            }
    
            multi_table.append("</tr>");
    
            // Create multiplication vertical table header
            for(int i = low_num; i <= factor_rows; i++)
            {
                multi_table.append("<tr>");
                multi_table.append("<th>"+Integer.toString(i)+"</th>");
    
                for(int j = low_num; j <= factor_columns; j++)
                {
                    cell_num = i * j;
    
                    // Apply scaling factor and assign key for proper color 
                    // Lowest number gets val in pos 0, highest gets highest val
                    if(i == low_num && j == low_num)
                    {

                        // ###############################################################################
                        // REGULAR CYCLE - 0
                        if(cycle == 0)
                        {
                            multi_table.append("<td>").append(cell_num).append("</td>");
                        }

                        // ###############################################################################
                        // GRAYSCALE CYCLE - 1
                        if(cycle == 1)
                        {
                            multi_table.append("<td class=\""+css_class_gray.get(0)+"\">").append(cell_num).append("</td>");
                        }

                        // ###############################################################################
                        // RAINBOW CYCLE - 2
                        if(cycle == 2)
                        {
                            multi_table.append("<td class=\""+css_class_rainbow.get(0)+"\">").append(cell_num).append("</td>");
                        }
                       
                    } 
                    else if(cell_num == high_num)
                    {

                        // ###############################################################################
                        // REGULAR CYCLE - 0
                        if(cycle == 0)
                        {
                            multi_table.append("<td>").append(cell_num).append("</td>");
                        }

                        // ###############################################################################
                        // GRAYSCALE CYCLE - 1
                        if(cycle == 1)
                        {
                            int last_key_gray = how_many_grays - 1;
                            multi_table.append("<td class=\""+css_class_gray.get(last_key_gray)+"\">").append(cell_num).append("</td>");
                        }

                        // ###############################################################################
                        // RAINBOW CYCLE - 2
                        if(cycle == 2)
                        {
                            int last_key_rainbows = how_many_colors - 1;
                            multi_table.append("<td class=\""+css_class_rainbow.get(last_key_rainbows)+"\">").append(cell_num).append("</td>");
                        }
                    }
                    else
                    {
                        switch(cycle)
                        {
                            // ###############################################################################
                            // REGULAR CYCLE - 0
                            case 0: 
                                multi_table.append("<td>").append(cell_num).append("</td>"); 
                                break;

                            // ###############################################################################
                            // GRAY CYCLE - 1
                            case 1: 
                                gray_key = (int)Math.ceil(cell_num * scaling_factor_gray);
                                multi_table.append("<td class=\""+css_class_gray.get(gray_key)+"\">").append(cell_num).append("</td>");
                                break;

                            // ###############################################################################
                            // RAINBOW CYCLE - 2
                            case 2:
                                color_key = (int)Math.ceil(cell_num * scaling_factor_rainbow);
                                multi_table.append("<td class=\""+css_class_rainbow.get(color_key)+"\">").append(cell_num).append("</td>");
                                break;
                        }
                    }
                }

                multi_table.append("</tr>");

            }
    
            // ################################################################################
            // Init HTML 
            System.out.println("<!DOCTYPE html>");
            System.out.println("<html>");
            System.out.println("<head>");
            System.out.println("<title>CSC 635 Lab 6 - ROMAN CAMPBELL</title>");
            System.out.println("<style>");
            System.out.println(css_content.toString());
            System.out.println("</style>");
            System.out.println("</head>");
            System.out.println("<body>");
    
            // ################################################################################
            // Construct HTML table
            html_table.append("<table>");
            html_table.append("<thead>").append(multi_table).append("</thead>");
            html_table.append("</table>");

            // Reset for next iteration
            multi_table.setLength(0);
        }
     

        // Convert to string and display table
        System.out.println(html_table.toString());

        // TABLE FOOTER
        System.out.println("<footer>");
        System.out.println("<a href=\"../css/stylesheets.css\" target=\"_blank\" alt=\"Stylesheets\">Stylesheets.css</a>");
        System.out.println("<a href=\"./Main.java\" target=\"_blank\" alt=\"Java sourcecode\">Main.java</a>");
        System.out.println("</footer>");

        System.out.println("</body>");
        System.out.println("</html>");

    }
}