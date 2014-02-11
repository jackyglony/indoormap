package com.indoormap.framework.common;
//���������뵽�ҵĲ������� http://uoloveruo.blog.163.com
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import android.util.Log;

import com.indoormap.framework.model.Map;
import com.indoormap.framework.model.Point;

public class AStart {
	
	private byte canMoveIndex = 1;	//����ͨ�еĵ�ͼͼ������
	
//	private int cellSize = 20;
//	
//	private int mapHeight = 480;
	
	private byte tileSize = 1;	//ͼ���С
	
	private int rows = 0;			//��ͼ����
	
	private int cols = 0;			//��ͼ����
	
	private byte[][] map;		//��ͼ����
	
	private final byte G_OFFSET = 1;	//ÿ��ͼ��Gֵ������ֵ
	
	private int destinationRow;	//Ŀ��������
	private int destinationCol;	//Ŀ��������
	
	Vector<Node> closeNode = new Vector<Node>();	//�ڵ�ر��б�
	
	Vector<Node> openNode = new Vector<Node>();		//�ڵ㿪�����
	

	public AStart(){
		
	}
	
	//���õ�ͼ��Ϣ���� 
	public void setMap(byte[][] map, int rows, int cols){
		this.map = map;
		this.rows = rows;
		this.cols = cols;
		closeNode.removeAllElements();
		openNode.removeAllElements();
	}
	
	public ArrayList<Point> getPath(int startX, int startY, int destinationX, int destinationY ,Map map){
		long timer = System.currentTimeMillis();
		destinationRow = getRowPosition(destinationY);
		destinationCol = getColPosition(destinationX);
		long timeout = 5000;
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		Node startNode = new Node();
		startNode.row = getRowPosition(startY);
		startNode.col = getColPosition(startX);
		startNode.g = 0;
		startNode.h = getH(startNode.row, startNode.col);
		startNode.f = startNode.g + startNode.h;
		openNode.add(startNode);
		Node bestNode;
		while(true){
			bestNode = getBesetNode();
			if(bestNode == null){		//δ�ҵ�·��
				return null;
			}else if(bestNode.row == getRowPosition(destinationY) 
					&& bestNode.col == getColPosition(destinationX)){
				long time = System.currentTimeMillis() - timer;
				System.out.println("������������������ " + time);
				ArrayList<Point> result = new ArrayList<Point>();
				Node _Node = bestNode;
				while(_Node.parent != null){
//					System.out.println("x: " + _Node.col + "  y: " + _Node.row);
					result.add(new Point(translateMartrixToMap(_Node.col,false,map),translateMartrixToMap( _Node.row,true,map),map));
					_Node = _Node.parent;
				}
				result.add(new Point(translateMartrixToMap(startX,false,map), translateMartrixToMap(startY,true,map),map));
				Collections.reverse(result);
				return result;
			}
			seachSeccessionNode(bestNode);
			endTime = System.currentTimeMillis();
			Log.i("aStart-BestNode", bestNode.toString());
			if(endTime - startTime > timeout){
//			    return null;
			}
		}
		
	}
	
	/**
	 * ���ݴ���Ľڵ������ӽڵ�
	 * @param bestNode
	 * @param destinationRow
	 * @param destinationCol
	 */
	private void seachSeccessionNode(Node bestNode){
		int row, col;
		//�ϲ��ڵ�
		if(!isOutOfMap(bestNode.row - 1, bestNode.col)){
		if(isCanMove(row = bestNode.row - 1, col = bestNode.col)){
			creatSeccessionNode(bestNode, row, col);
		}
		}
		//�²��ڵ�
		if(!isOutOfMap(bestNode.row + 1, bestNode.col)){
		if(isCanMove(row = bestNode.row + 1, col = bestNode.col)){
			creatSeccessionNode(bestNode, row, col);
		}
		}
		//�󲿽ڵ�
		if(!isOutOfMap(bestNode.row, bestNode.col-1)){
		if(isCanMove(row = bestNode.row, col = bestNode.col - 1)){
			creatSeccessionNode(bestNode, row, col);
		}
		}
		//�Ҳ��ڵ�
		if(!isOutOfMap(bestNode.row, bestNode.col+1)){
		if(isCanMove(row = bestNode.row, col = bestNode.col + 1)){
			creatSeccessionNode(bestNode, row, col);
		}
		}
		closeNode.addElement(bestNode);
		for(int i = 0; i < openNode.size(); i ++){
			if(openNode.elementAt(i).row == bestNode.row 
					&& openNode.elementAt(i).col == bestNode.col){
				openNode.removeElementAt(i);
				break;
			}
		}
	}
	
	private void creatSeccessionNode(Node bestNode, int row, int col){
		Node oldNode = null;
		int g = bestNode.g + G_OFFSET;
		if(!isInClose(row, col)){
			if((oldNode = checkOpen(row, col)) != null){
				if(oldNode.g < g){
					oldNode.parent = bestNode;
					oldNode.g = g;
					oldNode.f = g + oldNode.h;
				}
			}else{
				Node node = new Node();
				node.parent = bestNode;
				node.g = g;
				node.h = getH(row, col);
				node.f = node.g + node.h;
				node.row = row;
				node.col = col;
				openNode.addElement(node);
			}
		}
	}
	
	
	private Node checkOpen(int row, int col){
		Node node = null;
		for(int i = 0; i < openNode.size(); i ++){
			if(openNode.elementAt(i).row == row && openNode.elementAt(i).col == col){
				node = openNode.elementAt(i);
				return node;
			}
		}
		return node;
	}
	
	private boolean isInClose(int row, int col){
		for(int i = 0; i < closeNode.size(); i ++){
			if(closeNode.elementAt(i).row == row && closeNode.elementAt(i).col == col){
				return true;
			}
		}
		return false;
	}
	
	//�õ����Žڵ�
	private Node getBesetNode(){
		Node bestNode = null;
		int f = 999999999;
		for(int i = 0; i < openNode.size(); i ++){
			if(openNode.elementAt(i).f < f){
				f = openNode.elementAt(i).f;
				bestNode = openNode.elementAt(i);
			}
		}
		return bestNode;
	}
	
	//�õ���ͼ���Hֵ
	private int getH(int row, int col){
		return (Math.abs(destinationRow - row) + Math.abs(destinationCol - col));
	}
	
	//�õ���λ�����ڵ�ͼ��
	private int getRowPosition(int y){
		return (y / tileSize);
	}
	
	//�õ���λ�����ڵ�ͼ��
	private int getColPosition(int x){
		return (x / tileSize);
	}
	
	private boolean isOutOfMap(int col , int row){
	    boolean result = false;
	    if(col < 0 || col >=rows ){
	        result = true;
	    }
	    else if(row < 0 || row >= cols){
	        result = true;
	    }else{
	        result = false;
	    }
	    return result;
	}
	
	//����ͼ���Ƿ��ͨ��
	private boolean isCanMove(int col, int row){
	    byte mapItem = map[col][row];
	    boolean result = false;
		if(mapItem == canMoveIndex){
		    result = true;
		}
		else {
		    result = false;
		}
		return result;
	}

    private float translateMartrixToMap(int input , boolean isHeight ,Map map){
	    float result = 0;
//	    if(isHeight){
//	        result = (float)(pp.getMapHeightWithoutZoom()-((input+0.5)*pp.getCellSizeWithOutZoom()));
//	    }else{
	        result =  (float)((input+0.5)*map.getCellSize());
//	    }
	    
        return result;
    }
}
/**
 * �ڵ���
 * @author 
 *
 */
class Node{
	
	int f;	//�ýڵ�·������
	
	int g;	//����ʼ�㵽�ýڵ��Ԥ������
	
	int h;	//�Ӹýڵ㵽�յ�������پ��루�����ϰ�ˮƽ��ֱ�ƶ����յ�ľ��룩
	
	int row;	//�ýڵ�������
	
	int col;	//�ýڵ�������
	
	Node parent;	//�ýڵ�ĸ��ڵ�
	
	Node[] child = new Node[8];	//�ýڵ���ӽڵ㣬���8��
	
	@Override
	public String toString()
	{
	    return "x : " +col+" , y : "+row;
	}
}