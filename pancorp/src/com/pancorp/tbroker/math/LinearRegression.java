package com.pancorp.tbroker.math;

import java.util.ArrayDeque;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Utils;

public class LinearRegression { 
	private static Logger lg = LogManager.getLogger(LinearRegression.class);
	
    public static double calculateLinearRegressionSlope(ArrayDeque<Candle> q, Candle cn) { 
        int MAXN = q.size(); //1000;
        int n = 0;
        Candle c;
        double[] x = new double[MAXN];
        double[] y = new double[MAXN];

        // first pass: read in data, compute xbar and ybar
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        Iterator<Candle> it = q.iterator();
        while(it.hasNext()) { //!q.isEmpty()) {
        	c = (Candle)it.next();
            x[n] = n+1; // StdIn.readDouble();
            if(lg.isTraceEnabled())
            	lg.trace("x["+n+"]:"+x[n]);
            y[n] = c.close(); //StdIn.readDouble();
            if(lg.isTraceEnabled())
            	lg.trace("y["+n+"]:"+y[n]);
            sumx  += x[n];
            sumx2 += x[n] * x[n];
            sumy  += y[n];
            n++;
        }
        double xbar = sumx / n;
        double ybar = sumy / n;
        if(lg.isTraceEnabled())
        	lg.trace("xbar: " + xbar + ", ybar="+ybar);

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        double beta1 = xybar / xxbar;
        double beta0 = ybar - beta1 * xbar;

        // print results
        if(lg.isDebugEnabled())
        lg.debug("y   = " + beta1 + " * x + " + beta0);

        // analyze results
        int df = n - 2;
        double rss = 0.0;      // residual sum of squares
        double ssr = 0.0;      // regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = beta1*x[i] + beta0;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }
        double R2    = ssr / yybar;
        double svar  = rss / df;
        double svar1 = svar / xxbar;
        double svar0 = svar/n + xbar*xbar*svar1;
        if(lg.isDebugEnabled()) {
            lg.debug("R^2                 = " + R2);
            lg.debug("std error of beta_1 = " + Math.sqrt(svar1));
            lg.debug("std error of beta_0 = " + Math.sqrt(svar0));
        }
        
        svar0 = svar * sumx2 / (n * xxbar);
        
        if(lg.isDebugEnabled()) {
        	lg.debug("std error of beta_0 = " + Math.sqrt(svar0));

        	lg.debug("SSTO = " + yybar);
        	lg.debug("SSE  = " + rss);
        	lg.debug("SSR  = " + ssr);
        }
        
        return beta0;
    }
    
    public static void main(String[] args){
    	LinearRegression.calculateLinearRegressionSlope(Utils.fillTheQueue(72, Constants.TFU_MIN, 10, 27.8), new Candle(0L, 28.5, 28.4, 28.3, 28.5,28.6,300000L,0));
    }
}
