// Generated code from Butter Knife. Do not modify!
package com.xl.voteapp.base;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseListFragment$$ViewInjector {
  public static void inject(Finder finder, final com.xl.voteapp.base.BaseListFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493013, "field 'mErrorLayout'");
    target.mErrorLayout = (com.xl.voteapp.ui.empty.EmptyLayout) view;
  }

  public static void reset(com.xl.voteapp.base.BaseListFragment target) {
    target.mErrorLayout = null;
  }
}
