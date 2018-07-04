package com.boc.jx.baseUtil.cache;

import java.util.LinkedList;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.bean.AcitivtyBean;

/**
 * 缓存容器对象
 * Created by huwentao on 2014/4/30.
 */
public class CacheLinkedMap<K, V> extends LinkedList<AcitivtyBean> {

    public CacheLinkedMap(int MAX_SIZE) {
        this.MAX_SIZE = MAX_SIZE;
    }


    private int MAX_SIZE = 100;
    
     public void put(K key,V value) {
		// TODO 放入对象
    	 AcitivtyBean bean = new AcitivtyBean(); 
    	 bean.setActivityName((String)key);
    	 bean.setActivity((BaseActivity)value);
    	 add(bean);
	}
     
     
     public V get(K key){
    	 for (int i = 0; i < size(); i++) {
    		 AcitivtyBean bean = get(i);
    		 if(bean.getActivityName().equals(key)){
    			 return (V) get(i).getActivity();
    		 }
		}
		return null;
     }
     
     public V removeCache(K key){
    	 for (int i = 0; i < size(); i++) {
    		 AcitivtyBean bean = get(i);
    		 if(bean.getActivityName().equals(key)){
    			 return  (V) ((AcitivtyBean) remove(i)).getActivity();
    		 }
		}
    	 return null;
     }
    
    
//    public void removeLastItem(){
//    	Set<K> set=keySet();
//    	Iterator<K> iterator=set.iterator();
//    	while (iterator.hasNext()) {
//    		String key = (String) iterator.next();
//    		
//			
//		}
//    }
    
    
}
