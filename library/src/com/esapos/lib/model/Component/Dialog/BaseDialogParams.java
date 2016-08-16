package com.esapos.lib.model.Component.Dialog;



/**
 * @description: 对话框属性
 *
 */
public class BaseDialogParams {
	
	private String _title;
	private float _titleSize;
	private int _titleColor;
	private int _topViewBgResId;
	private int _topViewBgColor;
	private int _dialogBgResId;
	private int _bottomViewBgResId;
	private String _content;
	private float _contentSize;
	private int _contentColor;
	private int _lineColor;
	private int width;
	private int height;
	protected boolean cancel=true;
	protected int gravity=-1;

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		_content = content;
	}

	public int getTopViewBgResId() {
		return _topViewBgResId;
	}

	public void setTopViewBgResId(int topViewBgResId) {
		_topViewBgResId = topViewBgResId;
	}

	public int getDialogBgResId() {
		return _dialogBgResId;
	}

	public void setDialogBgResId(int dialogBgResId) {
		_dialogBgResId = dialogBgResId;
	}

	public int getBottomViewBgResId() {
		return _bottomViewBgResId;
	}

	public void setBottomViewBgResId(int bottomViewBgResId) {
		_bottomViewBgResId = bottomViewBgResId;
	}

	public int getTopViewBgColor() {
		return _topViewBgColor;
	}

	public void setTopViewBgColor(int topViewBgColor) {
		_topViewBgColor = topViewBgColor;
	}

	/**
	 * @return the _titleSize
	 */
	public float getTitleSize() {
		return _titleSize;
	}

	public void setTitleSize(float titleSize) {
		_titleSize = titleSize;
	}

	/**
	 * @return the _titleColor
	 */
	public int getTitleColor() {
		return _titleColor;
	}

	public void setTitleColor(int titleColor) {
		_titleColor = titleColor;
	}

	/**
	 * @return the _contentSize
	 */
	public float getContentSize() {
		return _contentSize;
	}

	public void setContentSize(float contentSize) {
		_contentSize = contentSize;
	}

	/**
	 * @return the _contentColor
	 */
	public int getContentColor() {
		return _contentColor;
	}

	public void setContentColor(int contentColor) {
		_contentColor = contentColor;
	}

	/**
	 * @return the _lineColor
	 */
	public int getLineColor() {
		return _lineColor;
	}

	public void setLineColor(int lineColor) {
		_lineColor = lineColor;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public boolean getCancel() {
		return cancel;
	}


	public int getGravity() {
		return gravity;
	}
}
