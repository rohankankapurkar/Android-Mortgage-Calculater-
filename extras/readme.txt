Lab 2
Mortgage Calculator
by
Rohan Kankapurkar 011419707
Shruti Loya 011427780

We have implemented navigation drawer to choose the fragment. In this we have designed two fragments one for calculating the mortgage of property. Another fragment is used to display the map view. When user enters the details to calculate mortgage the entries are first validated. If the valid entries are put the application will calculate the mortgage. The application has three buttons on the first fragment. One to calculate the mortgage, one to reset the contents and one to save the mortgage calculator. Here user will be only able to save the mortgage calculation for particular property if the valid address is entered in the application. The address entered is validated with google map API . If the Google Map API return the latitude and longitude for the address located (geocoder) if the address is valid. Otherwise the application toasts the invalid address screen. 
If the valid address is entered . the address will be saved in the SQLite. On the map fragment we are showing google map view. Where all the entries from SQlite are fetched and mapped on the google map with their respective associated values. 

After clicking the marker the marker pops out and asks the user to either edit the property details or to delete the property. On clicking the edit button application takes the user to the first fragment to edit the details of the property. On clicking the delete button the marker gets deleted from the SQLite and the hence from the map. 



 


