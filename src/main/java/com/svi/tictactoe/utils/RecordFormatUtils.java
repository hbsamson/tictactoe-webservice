package com.svi.tictactoe.utils;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
public final class RecordFormatUtils {
 private RecordFormatUtils() {}
 public static String gameToCsv(GameRecordDTO r) { if (r.getPlayerName()!=null&&!r.getPlayerName().isEmpty()) return String.format("%s,%s,%s,%s,%s,%s",r.getGameId(),r.getPlayerId(),csv(r.getPlayerName()),r.getSymbol(),r.getLocation(),r.getDateSaved()); return String.format("%s,%s,%s,%s,%s",r.getGameId(),r.getPlayerId(),r.getSymbol(),r.getLocation(),r.getDateSaved()); }
 public static GameRecordDTO gameFromCsv(String line) { String[] p=parse(line); if(p.length==5)return new GameRecordDTO(p[0],p[1],p[2],p[3],p[4]); if(p.length==6)return new GameRecordDTO(p[0],p[1],p[2],p[3],p[4],p[5]); throw new IllegalArgumentException("Invalid Record format"); }
 public static RoomDTO roomFromCsv(String id,String line) { String[] p=line.split(",",-1); if(p.length!=2)throw new IllegalArgumentException("Invalid Room record format"); return new RoomDTO(id,p[0],p[1]); }
 private static String csv(String v){return v.indexOf(',')>=0||v.indexOf('"')>=0?'"'+v.replace("\"","\"\"")+'"':v;}
 private static String[] parse(String l){java.util.List<String>o=new java.util.ArrayList<>();StringBuilder b=new StringBuilder();boolean q=false;for(int i=0;i<l.length();i++){char c=l.charAt(i);if(c=='"'){if(q&&i+1<l.length()&&l.charAt(i+1)=='"'){b.append('"');i++;}else q=!q;}else if(c==','&&!q){o.add(b.toString());b.setLength(0);}else b.append(c);}if(q)throw new IllegalArgumentException("Unclosed CSV field");o.add(b.toString());return o.toArray(new String[0]);}
}
