class Author{
    private String name;
    private String yearActive;

    public Author(String name , String yearActive){
        this.name = name;
        this.yearActive = yearActive;

    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name + "(" +yearActive +")";
    }
}
