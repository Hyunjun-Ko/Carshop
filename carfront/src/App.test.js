import { render, screen, fireEvent } from "@testing-library/react";
import App from "./App";
import TestRenderer from "react-test-renderer";
import AddCar from "./components/AddCar";

test("open add car modal form", async () => {
  render(<App />);
  fireEvent.click(screen.getByText("New Car"));
  expect(screen.getByRole("dialog")).toHaveTextContent("New car");
});

test("renders a snapeshot", () => {
  const tree = TestRenderer.create(<AddCar />).toJSON();
  expect(tree).toMatchSnapshot();
});
