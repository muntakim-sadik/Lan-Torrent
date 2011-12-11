

import java.io.*;


public class FileProcessor {
    private String filePath;
    private RandomAccessFile file;

    FileProcessor(String fileName, long size){
        filePath = fileName;
        try{
            file = new RandomAccessFile(filePath,"r");
        }catch(FileNotFoundException e){
            try{
                file = new RandomAccessFile(filePath,"rw");
                file.setLength(size);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }
    }

    public void closeFile(){
        try{
            file.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //if toDo==true then write the ary to the file starting from pos
    //if toDo==false then read ary from the file startin from pos
    //in both cases return the pos after the file has been read
    public synchronized long process(byte ary[],int length,boolean toDo,long pos){
        if(toDo)
            return saveToFile(ary,length,pos);
        else
            return readFromFile(ary,length,pos);
    }

    private long saveToFile(byte ary[],int length, long pos){
        long newPos=pos;
        try{
            file.write(ary,(int)pos,length);
            newPos = file.getFilePointer();
        }catch(IOException e){
            e.printStackTrace();
        }
        return newPos;
    }

    private long readFromFile(byte ary[],int length,long pos){
        long newPos = pos;
        try{
            file.read(ary, (int)pos, length);
            newPos = file.getFilePointer();
        }catch(IOException e){
            e.printStackTrace();
        }
        return pos;
    }
}
