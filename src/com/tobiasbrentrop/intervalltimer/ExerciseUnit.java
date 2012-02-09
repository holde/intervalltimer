package com.tobiasbrentrop.intervalltimer;

public class ExerciseUnit {

		private String exerciseName;
		private int preparationTime;
		private int durationTime;
		private int repetitions;
		private int restTime;
		private int totalTime;
		private int totalExerciseTime;
		private int totalRestTime;
		private int count;
		private int[] timesArray;
		
		public ExerciseUnit(String name, int preparationTime, int durationTime, int repetitions, int restTime) {
			this.exerciseName = name;
			this.durationTime = durationTime;
			this.preparationTime = preparationTime;
			this.repetitions = repetitions;
			this.restTime = restTime;
			
			calculateTimes();
		}
		
		private void calculateTimes() {
			totalRestTime = restTime * repetitions + preparationTime;
			totalExerciseTime = durationTime * repetitions;
			totalTime = totalRestTime + totalExerciseTime;
			count = 1 + (repetitions * 2); 
			timesArray = new int[count];
			timesArray[0] = preparationTime;
			for (int i = 0; i < repetitions * 2; i += 2) {
				timesArray[i+1] = durationTime;
				timesArray[i+2] = restTime;
			}
		}
		
		public String getName() {
			return exerciseName;
		}
		
		public String toString() {
			return ""+preparationTime+"s prep / "+repetitions+" * "+durationTime+"s exercise / "+restTime+"s rest";
		}
		public int getPreparationTime() {
			return preparationTime;
		}
		public int getDuration() {
			return durationTime;
		}
		public int getRepetitions() {
			return repetitions;
		}
		public int getRestTime() {
			return restTime;
		}
		public int getTotalTime() {
			return totalTime;
		}
		public int getTotalExerciseTime() {
			return totalExerciseTime;
		}
		public int getTotalRestTime() {
			return totalRestTime;
		}
		public int[] getTimesArray() {
			return timesArray;
		}
		
		public int getCount() {
			return count;
		}
		
}
