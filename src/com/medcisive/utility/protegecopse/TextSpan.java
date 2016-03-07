package com.medcisive.utility.protegecopse;

/*
 * Use of start,end including or excluding CR's:
 * 1.  When dealing with positions of text within the original report text,
 *     use m_start, m_end;
 * 2.  When comparing with Knowtator output, positions of text are defined
 *     by m_startNoCR, m_endNoCR.
 */
public class TextSpan {

    private int m_start;            // start pos.  (including count of CR's)
    private int m_end;              // end pos.    (including count of CR's)
    private int m_startNoCR;        // start pos.  (excluding count of CR's)
    private int m_endNoCR;          // end pos.    (excluding count of CR's)

    public TextSpan(int st, int end, int stNoCR, int endNoCR) {
        m_start = st;
        m_end = end;
        m_startNoCR = stNoCR;
        m_endNoCR = endNoCR;
    }

    public TextSpan(int start, int end) {
        m_start = start;
        m_end = end;
    }

    public int getStart() {
        return m_start;
    }

    public int getEnd() {
        return m_end;
    }

    public int getStartNoCR() {
        return m_startNoCR;
    }

    public int getEndNoCR() {
        return m_endNoCR;
    }

    @Override
    public String toString() {
        return "[" + m_start + "," + m_end + "]";
    }
}