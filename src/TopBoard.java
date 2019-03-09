import java.io.Serializable;
import java.util.ArrayList;

public class TopBoard implements Serializable{
    private ArrayList<Record> easy = new ArrayList<>();
    private ArrayList<Record> normal = new ArrayList<>();
    private ArrayList<Record> hard = new ArrayList<>();

    public void addToEasy(Record record){
        if (easy.size() == 0){
            easy.add(record);
        }else if (easy.size() < 10){
            for (int i = 0; i < easy.size(); i++){
                if (record.getTime() <= easy.get(i).getTime()){
                    easy.add(i, record);
                    break;
                }else if (i == easy.size()-1){
                    easy.add(record);
                    break;
                }
            }
        }else {
            for (int i = 0; i < 10; i++){
                if (record.getTime() < easy.get(i).getTime()){
                    easy.add(i, record);
                    break;
                }
            }
        }
    }
    public void addToNormal(Record record){
        if (normal.size() == 0){
            normal.add(record);
        }else if (normal.size() < 10){
            for (int i = 0; i < normal.size(); i++){
                if (record.getTime() < normal.get(i).getTime()){
                    normal.add(i, record);
                    break;
                }else if (i == normal.size()-1){
                    normal.add(record);
                    break;
                }
            }
        }else {
            for (int i = 0; i < 10; i++){
                if (record.getTime() < normal.get(i).getTime()){
                    normal.add(i, record);
                    break;
                }
            }
        }
    }
    public void addToHard(Record record){
        if (hard.size() == 0){
            hard.add(record);
        }else if (hard.size() < 10){
            for (int i = 0; i < hard.size(); i++){
                if (record.getTime() < hard.get(i).getTime()){
                    hard.add(i, record);
                    break;
                }else if (i == hard.size()-1){
                    hard.add(record);
                    break;
                }
            }
        }else {
            for (int i = 0; i < 10; i++){
                if (record.getTime() < hard.get(i).getTime()){
                    hard.add(i, record);
                    break;
                }
            }
        }
    }

    public ArrayList<Record> getEasy() {
        return easy;
    }

    public ArrayList<Record> getNormal() {
        return normal;
    }

    public ArrayList<Record> getHard() {
        return hard;
    }
}
