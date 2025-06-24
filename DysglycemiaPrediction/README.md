# DysglycemiaPrediction

For the best understanding of the project, please read the report first, then this readme, then the code files/ipynb notebooks themselves.

## Machine Learning Models

## Deep Learning Models

### Setup Part 1 (Optional)

This setup is not required, it's only there if you don't already have a setup to view Jupyter Notebooks, otherwise you may skip these steps (unless errors occur, then follow these steps).

1. You may download and install Anaconda Navigator from https://www.anaconda.com/products/navigator.
2. Open Anaconda Prompt from the start menu (which is a terminal)
3. Run these commands in order:

- conda create -n myenv python=3.9 numpy pandas matplotlib seaborn scikit-learn jupyter
- conda activate myenv
- pip install tensorflow==2.10.0
- pip install keras-tuner

4. You may close the anaconda prompt. Open Anaconda Navigator. From the middle dropdown at the top, select the "myenv" environment.
5. Find Jupyter Notebook from the applications below and run it.
6. You should be able to navigate to the ipynb files from our project to open and run them.

### Setup Part 2 (Required for code execution)

If you wish to reproduce the results found in the notebooks, first put the MIMIC-IV dataset (in gzip format) in the root folder of the project or "/". An example would just be a path like "mimic-iv-3.1/mimic-iv-3.1/icu/chartevents.csv.gz" which the code will use. Then run all notebooks in order shown below. You may skip the LSTM Model.ipynb file entirely. Note this will take around 5+ hours on a local machine.

### Reading Order

The preferred reading order of the deep learning notebooks is from the simpler models to the more complex. Starting with the EDA and Datagen for DL. The "LSTM Model.ipynb" is an optional file to read, it is only used as a benchmark and it is uninteresting and unnecessary to run for our research/experimentation.

1. Small MIMIC EDA and LSTM Datagen.ipynb
2. LSTM Model.ipynb
3. Resampled LSTM Model.ipynb
4. Multivariate Resampled LSTM Model.ipynb
5. Multivariate Decomp Split LSTM Model.ipynb

### Work Completion

Linear Regression: Zion Knight
Logistic Regression: Zion Knight
Random Forest: Oliver Smith
XGBoost: Andy Than
LSTM: Mashal Khan
SVR: Jannik Ernst
