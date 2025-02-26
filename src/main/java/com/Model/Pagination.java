package com.Model;


import java.util.List;

public class Pagination {
    private List<?> content;
    private int pageno;
    private int totalpage;
    private boolean pagefirst;
    private boolean pagelast;
    private boolean empty = false;

    public Pagination() {

    }

    public Pagination(List<?> content, int pageno, int totalpage, boolean pagefirst,
            boolean pagelast, boolean empty) {
        this.content = content;
        this.pageno = pageno;
        this.totalpage = totalpage;
        this.pagefirst = pagefirst;
        this.pagelast = pagelast;
        this.empty = empty;
    }

    public List<?> getContent() {
        return content;
    }

    public void setContent(List<?> content) {
        this.content = content;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public boolean isPagefirst() {
        return pagefirst;
    }

    public void setPagefirst(boolean pagefirst) {
        this.pagefirst = pagefirst;
    }

    public boolean isPagelast() {
        return pagelast;
    }

    public void setPagelast(boolean pagelast) {
        this.pagelast = pagelast;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

}
