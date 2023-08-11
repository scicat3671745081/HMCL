/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.ui.construct;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jackhuang.hmcl.setting.ConfigHolder;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.util.javafx.BindingMapping;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;

import static org.jackhuang.hmcl.util.Logging.LOG;

public class TwoLineListItem extends VBox {
    private static final String DEFAULT_STYLE_CLASS = "two-line-list-item";
    private static final double TITLE_PART = 0.7D;

    private static final class TagChangeListener implements ListChangeListener<String> {
        private final List<Node> list;
        private final Function<String, Node> mapper;
        private final Runnable callback;

        public TagChangeListener(List<Node> list, Function<String, Node> mapper, Runnable callback) {
            this.list = list;
            this.mapper = mapper;
            this.callback = callback;
        }

        @Override
        public void onChanged(Change<? extends String> change) {
            if (list != null) {
                while (change.next()) {
                    if (change.wasPermutated()) {
                        for (int i = change.getFrom(); i < change.getTo(); i++) {
                            list.set(i, mapper.apply(change.getList().get(i)));
                            list.set(i + list.size() / 2, mapper.apply(change.getList().get(i)));
                        }
                    } else {
                        if (change.wasRemoved()) {
                            list.subList(change.getFrom() + list.size() / 2, change.getFrom() + change.getRemovedSize() + list.size() / 2).clear();
                            list.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                        }
                        if (change.wasAdded()) {
                            for (int i = 0; i < change.getAddedSubList().size(); i++) {
                                list.add(change.getFrom() + i + list.size() / 2, mapper.apply(change.getAddedSubList().get(i)));
                                list.add(change.getFrom() + i, mapper.apply(change.getAddedSubList().get(i)));
                            }
                        }
                    }
                }
            }

            Platform.runLater(callback);
        }
    }

    private final StringProperty title = new SimpleStringProperty(this, "title");
    private final ObservableList<String> tags = FXCollections.observableArrayList();
    private final StringProperty subtitle = new SimpleStringProperty(this, "subtitle");

    private final Label titleLabel;
    private final HBox tagBox;
    private final Label subTitleLabel;

    private final BooleanProperty titleTranslatePlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty tagTranslatePlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty subTitleTranslatePlaying = new SimpleBooleanProperty(false);

    public TwoLineListItem(String titleString, String subtitleString) {
        this();

        title.set(titleString);
        subtitle.set(subtitleString);

        Platform.runLater(this::reLayout);
    }

    public TwoLineListItem() {
        setMouseTransparent(true);

        HBox firstLine = new HBox();
        firstLine.getStyleClass().add("first-line");

        {
            Pane titlePane = new Pane();
            titlePane.getStyleClass().add("title-pane");

            titleLabel = new Label();
            titleLabel.getStyleClass().add("title");
            titleLabel.textProperty().bind(BindingMapping.of(title).map(s -> s + " | " + s));
            title.addListener(observable -> Platform.runLater(this::reLayout));

            Rectangle titleClip = new Rectangle(0, 0, titleLabel.getWidth() / 2 - 10, titleLabel.getHeight());
            titleClip.widthProperty().bind(BindingMapping.of(titleLabel.widthProperty()).map(w -> w.doubleValue() / 2 - 10));
            titlePane.setClip(titleClip);
            titlePane.getChildren().setAll(titleLabel);

            Pane tagLabelsPane = new Pane();
            tagLabelsPane.getStyleClass().add("tag-pane");

            tagBox = new HBox();
            tags.addListener(new TagChangeListener(tagBox.getChildren(), txt -> {
                Label tagLabel = new Label();
                tagLabel.getStyleClass().add("tag");
                tagLabel.setText(txt);
                HBox.setMargin(tagLabel, new Insets(0, 8, 0, 0));
                return tagLabel;
            }, this::reLayout));

            // Stable method, but it's quite slow.
//            tags.addListener((ListChangeListener<? super String>) observable -> {
//                ArrayList<Node> list = new ArrayList<>(tags.size() * 2);
//                for (int i = 0; i < 2; i++) {
//                    for (String tag : tags) {
//                        Label tagLabel = new Label();
//                        tagLabel.getStyleClass().add("tag");
//                        tagLabel.setText(tag);
//                        HBox.setMargin(tagLabel, new Insets(0, 8, 0, 0));
//                        list.add(tagLabel);
//                    }
//                }
//                tagBox.getChildren().setAll(list);
//                Platform.runLater(this::reLayout);
//            });

            Rectangle tagClip = new Rectangle(0, 0, tagBox.getWidth() / 2 - 2, tagBox.getHeight());
            tagClip.widthProperty().bind(BindingMapping.of(tagBox.widthProperty()).map(w -> w.doubleValue() / 2 - 2));
            tagLabelsPane.setClip(tagClip);
            tagLabelsPane.getChildren().setAll(tagBox);

            firstLine.getChildren().setAll(titlePane, tagLabelsPane);
        }

        Pane secondLine = new Pane();
        {
            subTitleLabel = new Label();
            subTitleLabel.getStyleClass().add("subtitle");
            subTitleLabel.textProperty().bind(BindingMapping.of(subtitle).map(s -> s + " | " + s));
            subtitle.addListener(observable -> Platform.runLater(this::reLayout));

            Rectangle subTitleClip = new Rectangle(0, 0, subTitleLabel.getWidth() / 2 - 2, 80000);
            subTitleClip.widthProperty().bind(BindingMapping.of(subTitleLabel.widthProperty()).map(w -> w.doubleValue() / 2 - 2));
            secondLine.setClip(subTitleClip);

            secondLine.getChildren().setAll(subTitleLabel);
        }

        getChildren().setAll(firstLine, secondLine);

        FXUtils.onChangeAndOperate(subtitle, subtitleString -> {
            if (subtitleString == null) getChildren().setAll(firstLine);
            else getChildren().setAll(firstLine, secondLine);
        });

        getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.widthProperty().addListener(observable -> Platform.runLater(this::reLayout));

        TranslateTransition titleTranslateTransition = new TranslateTransition(Duration.seconds(0.1D), titleLabel);
        titleTranslateTransition.setFromX(0);
        titleTranslateTransition.durationProperty().bind(BindingMapping.of(titleLabel.widthProperty()).map(w -> Duration.seconds((w.doubleValue() / 2D + 1) / 30D)));
        titleTranslateTransition.toXProperty().bind(BindingMapping.of(titleLabel.widthProperty()).map(w -> -w.doubleValue() / 2D - 1));
        titleTranslateTransition.setInterpolator(Interpolator.LINEAR);
        titleTranslateTransition.setCycleCount(Animation.INDEFINITE);
        titleTranslatePlaying.addListener(generateTranslateListener(titleTranslateTransition));

        TranslateTransition tagTranslateTransition = new TranslateTransition(Duration.seconds(0.1D), tagBox);
        tagTranslateTransition.setFromX(0);
        tagTranslateTransition.durationProperty().bind(BindingMapping.of(tagBox.widthProperty()).map(w -> Duration.seconds(w.doubleValue() / 2D / 30D)));
        tagTranslateTransition.toXProperty().bind(BindingMapping.of(tagBox.widthProperty()).map(w -> -w.doubleValue() / 2D));
        tagTranslateTransition.setInterpolator(Interpolator.LINEAR);
        tagTranslateTransition.setCycleCount(Animation.INDEFINITE);
        tagTranslatePlaying.addListener(generateTranslateListener(tagTranslateTransition));

        TranslateTransition subTitleTranslateTransition = new TranslateTransition(Duration.seconds(0.1D), subTitleLabel);
        subTitleTranslateTransition.setFromX(0);
        subTitleTranslateTransition.durationProperty().bind(BindingMapping.of(subTitleLabel.widthProperty()).map(w -> Duration.seconds(w.doubleValue() / 2D / 30D)));
        subTitleTranslateTransition.toXProperty().bind(BindingMapping.of(subTitleLabel.widthProperty()).map(w -> (-w.doubleValue() - 8) / 2D));
        subTitleTranslateTransition.setInterpolator(Interpolator.LINEAR);
        subTitleTranslateTransition.setCycleCount(Animation.INDEFINITE);
        subTitleTranslatePlaying.addListener(generateTranslateListener(subTitleTranslateTransition));

        Rectangle mainClip = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        mainClip.widthProperty().bind(this.widthProperty());
        mainClip.heightProperty().bind(this.heightProperty());
        this.setClip(mainClip);

        this.minWidthProperty().set(0);
        HBox.setHgrow(this, Priority.SOMETIMES);
    }

    private static ChangeListener<Boolean> generateTranslateListener(TranslateTransition translateTransition) {
        if (ConfigHolder.config().isAnimationDisabled()) {
            return (observable, oldValue, newValue) -> {
            };
        } else {
            return (observable, oldValue, newValue) -> {
                if (oldValue.booleanValue() != newValue.booleanValue()) {
                    if (newValue) {
                        translateTransition.play();
                    } else {
                        translateTransition.jumpTo(Duration.ZERO);
                        translateTransition.stop();
                    }
                }
            };
        }
    }

    private void reLayout() {
        try {
            this.layout();
        } catch (NullPointerException npe) {
            LOG.log(Level.WARNING, "For some unknown reasons, invoking this::layout too fast may produce NullPointerException.", npe);
        }

        double titleMaxWidth = tags.size() == 0 ? this.getWidth() : this.getWidth() * TITLE_PART;
        double titleWidth = Math.min(titleLabel.getWidth() / 2, titleMaxWidth);
        double tagMaxWidth = this.getWidth() - titleWidth - this.paddingProperty().get().getLeft() - this.paddingProperty().get().getRight() - 5;
        double tagWidth = Math.min(tagBox.getWidth() / 2, tagMaxWidth);

        FXUtils.setLimitWidth((Pane) titleLabel.getParent(), titleWidth);
        Rectangle titleClip = new Rectangle(0, 0, titleWidth - 7, titleLabel.getHeight());
        titleLabel.getParent().setClip(titleClip);

        titleTranslatePlaying.set(titleLabel.getWidth() / 2 > titleMaxWidth);

        Rectangle tagClip = new Rectangle(0, 0, tagWidth - 2, tagBox.getHeight());
        tagBox.getParent().setClip(tagClip);

        tagTranslatePlaying.set(tagBox.getWidth() / 2 > tagMaxWidth);

        subTitleTranslatePlaying.set(subTitleLabel.getWidth() / 2 > this.getWidth());
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getSubtitle() {
        return subtitle.get();
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    public ObservableList<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
