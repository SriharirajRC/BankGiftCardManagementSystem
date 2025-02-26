package com.Utils;


import java.util.List;

import com.Model.Pagination;


public class PaginationFun4ty {
    public static Pagination getPage(int pageno, int pagesize, List<?> content, int totalelements) {

    	if(pagesize==0) {
    		pagesize=1;
    	}
        int totalpages = (totalelements / pagesize);
        if (totalelements % pagesize != 0)
            totalpages++;

        boolean pagefirst = (pageno == 0) ? true : false;

        boolean pagelast = (pageno == totalpages - 1) ? true : false;

        return new Pagination(
                content,
                pageno,
                totalpages,
                pagefirst,
                pagelast,
                content.size() == 0);
    }
}
