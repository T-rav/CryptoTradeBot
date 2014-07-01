/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.jautotrading;

import com.archean.jtradeapi.ApiWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class MarketRule implements Serializable {
    public RuleCondition.BaseCondition condition = null;
    public RuleAction.BaseAction action = null;

    public MarketRule(RuleCondition.BaseCondition condition, RuleAction.BaseAction action) {
        this.condition = condition;
        this.action = action;
    }

    public MarketRule(RuleCondition.BaseCondition condition) {
        this(condition, null);
    }

    public static class MarketRuleList extends ArrayList<MarketRule> { // Batch checker

        public static abstract class MarketRuleListCallback {
            public void onError(Exception e) {
                // nothing
            }

            abstract public void onSuccess(MarketRule rule);
        }

        public enum MarketRuleExecutionType {
            QUEUED, PARALLEL, ONLY_FIRST_SATISFIED
        }

        MarketRuleExecutionType executionType = MarketRuleExecutionType.PARALLEL;
        public transient MarketRuleListCallback callback = null;
        private transient Map<Object, Object> ruleData = new HashMap<>(); // Cache

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            ruleData = new HashMap<>();
        }

        public void checkRules(final ApiWorker worker) {
            RuleCondition.makeConditionData(ruleData, worker);
            ListIterator<MarketRule> ruleIterator = this.listIterator();
            while (ruleIterator.hasNext()) {
                try {
                    if (executionType == MarketRuleExecutionType.QUEUED && ruleIterator.nextIndex() != 0) {
                        break;
                    }
                    MarketRule rule = ruleIterator.next();
                    if (rule.condition.isSatisfied(ruleData)) {
                        if (rule.action != null) {
                            rule.action.apiWorker = worker;
                            Thread actionThread = new Thread(rule.action);
                            actionThread.run();
                        }
                        if (callback != null) {
                            callback.onSuccess(rule);
                        }
                        if (executionType == MarketRuleExecutionType.ONLY_FIRST_SATISFIED) {
                            this.clear();
                            break;
                        }
                        ruleIterator.remove(); // Rule executed
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e);
                    }
                }
            }
        }
    }
}
