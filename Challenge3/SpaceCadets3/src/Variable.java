public class Variable {

    private String varName;
    private Integer varValue;

    public Variable(String name){
        varName = name;
        varValue=0;
    }

    public void clear(){
        varValue=0;
        System.out.println(varName+" = "+varValue);
    }

    public void increment(){
        varValue+=1;
        System.out.println(varName+" = "+varValue);
    }

    public void decrement(){
        if(varValue!=0) {
            varValue -= 1;
            System.out.println(varName+" = "+varValue);
        }
    }

    public void setVarValue(int newValue){
        varValue=newValue;
        System.out.println(varName+" = "+varValue);
    }

    public String getVarName(){
        return varName;
    }

    public Integer getVarValue(){
        return varValue;
    }
}
