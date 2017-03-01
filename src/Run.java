import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Run 
{
	public static void main (String arg[])
	{
		new Run().Gui(); 
	}
	
	void Gui()
	{
		JFrame frm = new JFrame ("LZW");
		frm.setSize(500,300);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pnl = new JPanel();
		frm.add(pnl);
		
		pnl.setLayout(null);
		
		JLabel l = new JLabel("File path");
		l.setBounds(211, 51, 71, 31);
		pnl.add(l);
		
		JTextField tf = new JTextField();
		tf.setBounds(99, 91, 277, 33);
		pnl.add(tf);
		
		JButton b1 = new JButton("Compress");
		b1.setBounds(33, 177, 111, 51);
		pnl.add(b1);
		
		JButton b2 = new JButton("Decompress");
		b2.setBounds(333, 177, 111, 51);
		pnl.add(b2);
		
		b1.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				Comp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});
		
		b2.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				Decomp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});

		frm.setVisible(true);
		
	}
	
	void Comp (String path)
	{
		String doc = new Run().ReadFromFile (path), tmp;
		File comFile = new File ("comFile.txt");
		Map <String,Integer> dictionary = new HashMap <String,Integer>();
		int index, lastIndex=128;
		
		try
		{
			comFile.createNewFile();
			FileWriter write = new FileWriter (comFile);
			
			for(int i=0 ; i<doc.length() ; )
			{
				index=doc.charAt(i);
				tmp=""+doc.charAt(i++);
				
				for( ; i<doc.length() && dictionary.get(tmp+=doc.charAt(i))!=null ;
						index=dictionary.get(tmp),i++);
				
				dictionary.put(tmp, lastIndex++);
				write.write(index+" ");
			}
			
			write.close();
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	String ReadFromFile (String path)
	{
		String doc="", s;
		
		try
		{
			Scanner in = new Scanner (new File(path));
			
			while (in.hasNext())
			{
				s=in.next();
				doc+=s;
			}
			
			in.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
			System.exit(0);
		}
		
		return doc;
	}

	void Decomp (String path)
	{
		Map <Integer,String> dictionary = new HashMap <Integer,String>();
		String doc="", tmp="", save="";
		int index, lastIndex=128;
		
		try
		{
			Scanner in = new Scanner (new File (path));
			
			while (in.hasNextInt())
			{
				index = in.nextInt();
				
				if(index<128)
					tmp=""+(char)index;
				
				else if(dictionary.get(index)==null)
					tmp=save+save.charAt(0);
				
				else
					tmp=dictionary.get(index);
				
				doc+=tmp;
				
				if(save!="")
					dictionary.put(lastIndex++, save+tmp.charAt(0));
				
				save=tmp;				
			}
			
			in.close();
			
			File dataFile = new File ("decomFile.txt");
			dataFile.createNewFile();
			FileWriter write = new FileWriter (dataFile);
			write.write(doc);
			write.close();
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
		}
	}
}

//decomp    E:\WorkSpace\WorkSpace_J\LZW\comFile.txt
//comp		E:\WorkSpace\WorkSpace_J\LZW\data.txt