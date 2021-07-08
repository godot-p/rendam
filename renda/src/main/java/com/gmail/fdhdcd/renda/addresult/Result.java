package com.gmail.fdhdcd.renda.addresult;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * 測定結果の格納に使用されるクラス。秒数が1~10秒の整数値の場合にのみ使用されます。
 * データベースの都合上、測定日時のマイクロ秒単位以下の数値は切り捨てられます。
 * フィールドがnullになることはありません。
 */

public class Result implements Externalizable {

    private final ReadOnlyStringWrapper name;
    private final ReadOnlyIntegerWrapper clicks;
    private final ReadOnlyIntegerWrapper seconds;
    private final ReadOnlyDoubleWrapper cps;
    private final ReadOnlyObjectWrapper<LocalDateTime> time;
    private static final long serialVersionUID = Long.MIN_VALUE + 1;

    /**
     * 直列化からの復元時に内部的に使用されるコンストラクタ。
     * 外部からは使用しないでください。
     */
    @Deprecated
    public Result() {
        this.name = new ReadOnlyStringWrapper();
        this.clicks = new ReadOnlyIntegerWrapper();
        this.seconds = new ReadOnlyIntegerWrapper();
        this.cps = new ReadOnlyDoubleWrapper();
        this.time = new ReadOnlyObjectWrapper<>();
    }

    /**
     * 与えられた情報をもとに、新しいResultを生成します。
     * CPSはclicks / secondsで自動的に計算されます。
     * 測定日時は現在の日時になります。
     *
     * @param name 名前
     * @param clicks クリック数
     * @param seconds 測定秒数
     */
    public Result(String name, int clicks, int seconds) {
        this(name, clicks, seconds, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
    }

    /**
     * 与えられた情報をもとに、新しいResultを生成します。
     * CPSはclicks / secondsで自動的に計算されます。
     *
     * @param name 名前
     * @param clicks クリック数
     * @param seconds 測定秒数
     * @param time 測定日時
     */
    public Result(String name, int clicks, int seconds, LocalDateTime time) {
        this.name = new ReadOnlyStringWrapper(name);
        this.clicks = new ReadOnlyIntegerWrapper(clicks);
        this.seconds = new ReadOnlyIntegerWrapper(seconds);
        this.cps = new ReadOnlyDoubleWrapper((double)clicks / seconds);
        this.time = new ReadOnlyObjectWrapper<>(time.truncatedTo(ChronoUnit.MICROS));
    }

    public String getName() {
        return this.name.getValue();
    }

    public ReadOnlyStringProperty nameProperty() {
        return this.name.getReadOnlyProperty();
    }

    public int getSeconds() {
        return this.seconds.getValue();
    }

    public ReadOnlyIntegerProperty secondsProperty() {
        return this.seconds.getReadOnlyProperty();
    }

    public int getClicks() {
        return this.clicks.getValue();
    }

    public ReadOnlyIntegerProperty clicksProperty() {
        return this.clicks.getReadOnlyProperty();
    }

    public double getCps() {
        return this.cps.getValue();
    }

    public ReadOnlyDoubleProperty cpsProperty() {
        return this.cps.getReadOnlyProperty();
    }

    public LocalDateTime getTime() {
        return this.time.getValue();
    }

    public ReadOnlyObjectProperty<LocalDateTime> timeProperty() {
        return this.time.getReadOnlyProperty();
    }

    @Override
    public String toString() {
        return "Name:" + name + ", Time:" + seconds + ", Clicks:" + clicks + ", CPS:" + cps + ", Date and time you measured:" + time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name.get() == null ? 0 : name.get().hashCode());
        result = prime * result + (clicks.get());
        result = prime * result + (seconds.get());
        result = prime * result + (time.get() == null ? 0 : time.get().hashCode());
        return result;
    }

    /**
     *
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Result)) {
            return false;
        }
        Result other = (Result) obj;
        if (!name.get().equals(other.name.get())) {
            return false;
        }
        if (clicks.get() != other.clicks.get()) {
            return false;
        }
        if (seconds.get() != other.seconds.get()) {
            return false;
        }
        if (!time.get().equals(other.time.get())) {
            return false;
        }
        return true;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.name.get());
        out.writeInt(this.clicks.get());
        out.writeInt(this.seconds.get());
        out.writeObject(this.time.get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name.setValue((String)in.readObject());
        this.clicks.set(in.readInt());
        this.seconds.set(in.readInt());
        this.cps.set((double)clicks.get() / seconds.get());
        this.time.set((LocalDateTime)in.readObject());
    }

}
