package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

public class LyricsData {

        @Json(name ="sentences")
        
        private List<Sentence> sentences = null;
        @Json(name ="file")
        
        private String file;
        @Json(name ="beat")
        
        private String beat;
        @Json(name ="enabledVideoBG")
        
        private boolean enabledVideoBG;
        @Json(name ="defaultIBGUrls")
        
        private List<String> defaultIBGUrls = null;
        @Json(name ="defaultVBGUrls")
        
        private List<String> defaultVBGUrls = null;
        @Json(name ="BGMode")
        
        private int bGMode;

        /**
         * No args constructor for use in serialization
         *
         */
        public LyricsData() {
        }

        /**
         *
         * @param enabledVideoBG
         * @param bGMode
         * @param defaultIBGUrls
         * @param file
         * @param sentences
         * @param beat
         * @param defaultVBGUrls
         */
        public LyricsData(List<Sentence> sentences, String file, String beat, boolean enabledVideoBG, List<String> defaultIBGUrls, List<String> defaultVBGUrls, int bGMode) {
            super();
            this.sentences = sentences;
            this.file = file;
            this.beat = beat;
            this.enabledVideoBG = enabledVideoBG;
            this.defaultIBGUrls = defaultIBGUrls;
            this.defaultVBGUrls = defaultVBGUrls;
            this.bGMode = bGMode;
        }

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void setSentences(List<Sentence> sentences) {
            this.sentences = sentences;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getBeat() {
            return beat;
        }

        public void setBeat(String beat) {
            this.beat = beat;
        }

        public boolean isEnabledVideoBG() {
            return enabledVideoBG;
        }

        public void setEnabledVideoBG(boolean enabledVideoBG) {
            this.enabledVideoBG = enabledVideoBG;
        }

        public List<String> getDefaultIBGUrls() {
            return defaultIBGUrls;
        }

        public void setDefaultIBGUrls(List<String> defaultIBGUrls) {
            this.defaultIBGUrls = defaultIBGUrls;
        }

        public List<String> getDefaultVBGUrls() {
            return defaultVBGUrls;
        }

        public void setDefaultVBGUrls(List<String> defaultVBGUrls) {
            this.defaultVBGUrls = defaultVBGUrls;
        }

        public int getBGMode() {
            return bGMode;
        }

        public void setBGMode(int bGMode) {
            this.bGMode = bGMode;
        }

    }

