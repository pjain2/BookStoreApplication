package edu.stevens.cs522.entities;




        import edu.stevens.cs522.contracts.AuthorContract;
        import edu.stevens.cs522.contracts.BookContract;
        import android.content.ContentValues;
        import android.database.Cursor;
        import android.os.Parcel;
        import android.os.Parcelable;

public class Author implements Parcelable {

    // TODO Modify this to implement the Parcelable interface.

    // NOTE: middleInitial may be NULL!

    public String firstName;

    public String middleInitial;

    public String lastName;

    /*Method required by Parceable interface
     * */
    private Author(Parcel in) {
        this.firstName = in.readString();
        this.middleInitial = in.readString();
        this.lastName = in.readString();
    }


    public Author(String firstName, String middleInitial, String lastName){
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
    }

    public Author(String name){
        firstName = name;
    }

    public Author(Cursor cursor){
        //this.id = BookContract.getID(cursor);
        this.firstName = AuthorContract.getName(cursor);
    }

    public void writeToProvider(ContentValues values){
        AuthorContract.putName(values, firstName);

    }

    /*Takes the input as string and returns the authors array
     * */
    public static Author[] getAuthors(String inputText){

        Author authorArr[] = new Author[5];
        if(inputText != null && !inputText.equals("")){
            //Seperates the list of Authors by a ','
            String authorsInput[];
            if(inputText.indexOf(",") > 0){
                authorsInput = inputText.split(",");
            }
            else {
                authorsInput = inputText.split("\\"+String.valueOf(BookContract.SEPARATOR_CHAR));
            }

            for(int i = 0; i< authorsInput.length;i++){
				/*String authorSingle[] = authorsInput[i].split(" ");

				String firstname = authorSingle[0];
				String middlename = null;
				String lastname = null;
				if(authorSingle.length > 2 && authorSingle[1].length() == 1){
					middlename = authorSingle[1];
					lastname = authorSingle[2];
				}
				else if(authorSingle.length > 1) {
					lastname = authorSingle[1];
				}
				Author authorObj = new Author(firstname, middlename, lastname);*/
                Author authorObj = new Author(authorsInput[i]);
                authorArr[i] = authorObj;
            }
        }

        return authorArr;
    }

    /*Method required by Parceable interface
     * */
    public int describeContents() {
        return 0;
    }

    /*Method required by Parceable interface
     * */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(firstName);
        out.writeString(middleInitial);
        out.writeString(lastName);
    }

    /*Method required by Parceable interface
     * */
    public static final Parcelable.Creator<Author> CREATOR
            = new Parcelable.Creator<Author>() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

}
