import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.meng.parsing.xml.Opinion;
import com.meng.parsing.xml.Opinions;

public class XMLParsing {
	public static void main(String[] args) {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Opinions ops = (Opinions) unmarshaller.unmarshal(new File("1.xml"));
			List<Opinion> opn = ops.getOpinion();
			for(int i=0;i<opn.size();i++){
				System.out.println(opn.get(i).getOhRefs() +" : " + opn.get(i).getOpId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
