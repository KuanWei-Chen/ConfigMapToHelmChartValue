import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class test {
    public static void main(String[] args) {
        BufferedReader reader;
        LayerDTO root = genDTO(new File("C:\\Users\\heisenberg\\Desktop\\TPI\\VN\\middle-asm-cd\\roles\\middle-bo-cl\\templates\\configmap.yml"));
        print(root, 0);
    }

    public static LayerDTO genDTO(File file){
        BufferedReader reader;
        Map<String, LayerDTO>  layerDTOHashMap = new HashMap<>();
        LayerDTO root = new LayerDTO("value", layerDTOHashMap);
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                if(line.contains("{{") && line.contains("}}")){
                    String key = line.substring(line.indexOf("{{")+2, line.indexOf("}}")).trim();
                    String [] spilt = key.split("\\.");
                    for(int layerCount=0; layerCount<spilt.length; layerCount++)
                    {
                        LayerDTO currentDTO = root;
                        for(int layer = 0; layer < layerCount+1; layer++)
                        {
                            String currentKey = spilt[layer];
                            if(currentDTO.childs.get(currentKey)==null)
                            {
                                currentDTO.childs.put(currentKey, new LayerDTO(currentKey, new HashMap<>()));
                            }

                            currentDTO = currentDTO.childs.get(currentKey);
                        }
                    }
                }
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    public static void print(LayerDTO layerDTO, int layerCount){
        StringBuilder layerSpace = new StringBuilder();
        layerSpace.append("  ".repeat(Math.max(0, layerCount)));
        if(layerDTO!=null)
        {
            List<String> keySort = new ArrayList<>(layerDTO.childs.keySet());
            Collections.sort(keySort);
            for(String key:keySort)
            {
                System.out.println(layerSpace+ key + ":");
                print(layerDTO.childs.get(key), layerCount+1);
            }
        }
    }
}

class LayerDTO{
    public String key;
    public Map<String, LayerDTO> childs;
    public LayerDTO(String key, Map<String, LayerDTO> childs){
        this.key = key;
        this.childs = childs;
    }
}